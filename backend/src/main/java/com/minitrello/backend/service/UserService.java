package com.minitrello.backend.service;

import com.minitrello.backend.common.ErrorCode;
import com.minitrello.backend.dto.UserCreationRequest;
import com.minitrello.backend.dto.UserResponse;
import com.minitrello.backend.entity.User;
import com.minitrello.backend.exception.AppException;
import com.minitrello.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // Tự động Inject Repository và Mapper
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserResponse createUser(UserCreationRequest request) {
        log.info("Đang xử lý đăng ký cho user: {}", request.getUsername());

        // 1. Kiểm tra tồn tại
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // 2. Chuyển DTO sang Entity (Thủ công)
        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword()) // Sau này sẽ hash ở đây
                .email(request.getEmail())
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
}
