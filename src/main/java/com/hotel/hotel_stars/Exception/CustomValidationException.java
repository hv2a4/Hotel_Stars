package com.hotel.hotel_stars.Exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomValidationException extends RuntimeException {
    private List<ValidationError> errorMessages;

    public CustomValidationException(List<ValidationError> errorMessages) {
        super("Có lỗi xác thực xảy ra");
        this.errorMessages = errorMessages;
    }

    public List<ValidationError> getErrorMessages() {
        return errorMessages;
    }


}
