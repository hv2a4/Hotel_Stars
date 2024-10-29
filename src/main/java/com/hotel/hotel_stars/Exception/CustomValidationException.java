package com.hotel.hotel_stars.Exception;

import java.util.List;

public class CustomValidationException extends RuntimeException {
    private List<String> errorMessages;

    public CustomValidationException(List<String> errorMessages) {
        super("Có lỗi xác thực xảy ra");
        this.errorMessages = errorMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
