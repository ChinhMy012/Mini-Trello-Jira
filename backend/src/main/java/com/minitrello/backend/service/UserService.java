package com.minitrello.backend.service;

import com.minitrello.backend.common.ErrorCode;
import com.minitrello.backend.dto.request.LoginRequest;
import com.minitrello.backend.dto.request.UserCreationRequest;
import com.minitrello.backend.dto.response.LoginResponse;
import com.minitrello.backend.dto.response.UserResponse;
import com.minitrello.backend.entity.Role;
import com.minitrello.backend.entity.User;
import com.minitrello.backend.exception.AppException;
import com.minitrello.backend.repository.RoleRepository;
import com.minitrello.backend.repository.UserRepository;
import com.minitrello.backend.security.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor // Tự động Inject Repository và Mapper
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RoleRepository roleRepository;

    public UserResponse createUser(UserCreationRequest request) {
        log.info("Đang xử lý đăng ký cho user: {}", request.getUsername());

        // 1. Kiểm tra tồn tại
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // 2. Chuyển DTO sang Entity (Thủ công)
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword())) // <--- QUAN TRỌNG
                .email(request.getEmail())
                .tokenVersion(1) // Khởi tạo version đầu tiên
                .build();
        // 3. LOGIC MỚI: TỰ ĐỘNG GÁN QUYỀN ROLE_USER
        // Tìm quyền ROLE_USER trong DB. Nếu chưa có (chạy lần đầu), tự động tạo mới!
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));

        // Gán quyền cho user (Nhớ đảm bảo class User của bạn có thuộc tính 'roles' kiểu Set<Role>)
        user.setRoles(new HashSet<>(List.of(userRole)));
        // 4. Lưu vào DB
        user = userRepository.save(user);

        // 5. Chuyển Entity sang Response DTO (Thủ công)
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        log.info("Bắt đầu xử lý đăng nhập cho user: {}", request.getUsername());

        // 1. Tìm user
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.error("Đăng nhập thất bại: Không tìm thấy username {}", request.getUsername());
                    return new AppException(ErrorCode.USER_NOT_FOUND);
                });

        // 2. So khớp mật khẩu
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            log.warn("Đăng nhập thất bại: Sai mật khẩu cho user {}", request.getUsername());
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // 3. Cơ chế "Vào sau đẩy trước": Tăng version
        user.setTokenVersion(user.getTokenVersion() + 1);
        userRepository.save(user);
        log.info("Đã cập nhật token_version lên {} cho user {}", user.getTokenVersion(), user.getUsername());

        // 4. Tạo Token
        String token = jwtProvider.generateToken(user);
        log.info("Tạo JWT thành công cho user: {}", user.getUsername());

        return LoginResponse.builder()
                .authenticated(true)
                .token(token)
                .build();
    }

    public void logout(HttpServletResponse response) {
        // Tạo một cookie trùng tên nhưng thời gian sống (maxAge) bằng 0
        Cookie cookie = new Cookie("jwt", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // Lệnh xóa cookie ngay lập tức
        response.addCookie(cookie);
    }
}
