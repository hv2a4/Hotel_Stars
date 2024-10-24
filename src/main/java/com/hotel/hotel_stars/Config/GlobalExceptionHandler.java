package com.hotel.hotel_stars.Config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<String> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ex, WebRequest request) {
        // Ghi log chi tiết lỗi
        System.err.println("InternalAuthenticationServiceException: " + ex.getMessage());
        ex.printStackTrace(); // Ghi chi tiết stack trace vào console

        // Trả về phản hồi lỗi cho client
        return new ResponseEntity<>("Authentication failed: " + ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    // Bạn có thể thêm nhiều phương thức xử lý ngoại lệ khác tại đây nếu cần
}
