package com.minitrello.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @Column(name = "name", length = 50)
    private String name; // Ví dụ: "ROLE_USER", "ROLE_ADMIN"

    @Column(name = "description")
    private String description;
}