package com.minitrello.backend.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(500, "Lỗi hệ thống không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(400, "Người dùng đã tồn tại", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(404, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    BOARD_NOT_FOUND(404, "Không tìm thấy bảng công việc", HttpStatus.NOT_FOUND),
    USERNAME_INVALID(1001, "Username phải có ít nhất 4 ký tự", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1002, "Password phải có ít nhất 8 ký tự", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1003, "Email không đúng định dạng", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(401, "Sai thông tin đăng nhập", HttpStatus.BAD_REQUEST)
    ;

    private final int code;
    private final String message;
    private final HttpStatus statusCode;

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}