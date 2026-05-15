package com.minitrello.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder // Giúp tạo object nhanh theo style User.builder().username(...).build()
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractMappedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Board> boards;

    @Column(name = "token_version")
    private Integer tokenVersion = 1; // Mặc định là 1 khi mới tạo tài khoản

    // --- ĐÃ TÁCH ROLE RIÊNG ---
    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER) // Load user là load luôn role của họ
    @JoinTable(
            name = "user_roles", // Tên bảng trung gian
            joinColumns = @JoinColumn(name = "user_id"), // Khóa ngoại trỏ về bảng users
            inverseJoinColumns = @JoinColumn(name = "role_name") // Khóa ngoại trỏ về bảng roles
    )
    private Set<Role> roles = new HashSet<>();
}