package com.minitrello.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class ColumnRequest {
    @NotBlank(message = "Tên cột không được để trống")
    private String title;
}