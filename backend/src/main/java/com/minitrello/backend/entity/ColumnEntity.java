package com.minitrello.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "columns_entity")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ColumnEntity extends AbstractMappedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private int position; // Dùng để sắp xếp thứ tự cột (0, 1, 2...)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @OneToMany(mappedBy = "columnEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC") // Tự động sắp xếp task theo vị trí khi lấy ra
    private List<Task> tasks;
}