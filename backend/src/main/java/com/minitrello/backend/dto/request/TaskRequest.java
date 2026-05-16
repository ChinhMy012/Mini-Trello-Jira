package com.minitrello.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class TaskRequest {
    @NotBlank(message = "Tiêu đề thẻ không được để trống")
    private String title;
    private String content;
}