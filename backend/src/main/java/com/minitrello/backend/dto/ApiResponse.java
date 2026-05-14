package com.minitrello.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Ẩn các field null khi trả về JSON
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;
}