package com.minitrello.backend.config;

import com.minitrello.backend.dto.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true; // Áp dụng cho tất cả các Controller
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        // Nếu body đã là ApiResponse (lỗi từ ExceptionHandler) thì giữ nguyên
        if (body instanceof ApiResponse) {
            return body;
        }

        // Tự động bọc dữ liệu thành công vào ApiResponse
        return ApiResponse.builder()
                .code(1000)
                .data(body)
                .message("Success")
                .build();
    }
}