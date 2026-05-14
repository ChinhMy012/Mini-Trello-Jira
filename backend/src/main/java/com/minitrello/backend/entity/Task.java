package com.minitrello.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private int positionOrder;
    private LocalDateTime deadline;

    @ManyToOne
    @JoinColumn(name = "column_id")
    private ColumnEntity column;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;
}