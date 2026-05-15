package com.minitrello.backend.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BoardResponse {
    private Long id;
    private String title;
    private String description;
    private String ownerUsername; // Trả về tên chủ sở hữu thay vì nguyên object User
}