package com.minitrello.backend.service;

import com.minitrello.backend.common.ErrorCode;
import com.minitrello.backend.dto.request.LoginRequest;
import com.minitrello.backend.dto.request.UserCreationRequest;
import com.minitrello.backend.dto.response.LoginResponse;
import com.minitrello.backend.dto.response.UserResponse;
import com.minitrello.backend.entity.User;
import com.minitrello.backend.exception.AppException;
import com.minitrello.backend.repository.UserRepository;
import com.minitrello.backend.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // Tự động Inject Repository và Mapper
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

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

        // 3. Lưu vào DB
        user = userRepository.save(user);

        // 4. Chuyển Entity sang Response DTO (Thủ công)
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
}
