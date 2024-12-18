package com.hotel.hotel_stars.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailFeedback {
    private String  fullname;
    private String email;
    private String message;
}
