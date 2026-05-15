package com.minitrello.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "boards")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder // Dùng cho manual mapping
public class Board extends AbstractMappedEntity { // Kế thừa để có createdAt, updatedAt
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Tiêu đề không được để trống
    private String title;

    @Column(columnDefinition = "TEXT") // Cho phép mô tả dài
    private String description;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy để tối ưu hiệu năng
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ColumnEntity> columns;
}