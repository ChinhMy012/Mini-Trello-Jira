package com.minitrello.backend.repository;

import com.minitrello.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring sẽ tự hiểu và viết câu SQL tìm theo username cho bạn
    Optional<User> findByUsername(String username);

    // Kiểm tra tồn tại để dùng khi đăng ký
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}