package com.hotel.hotel_stars.Exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class ValidationError {

    private String field;
    private String message;

    public ValidationError(String message) {
        this.message = message;
    }
}

