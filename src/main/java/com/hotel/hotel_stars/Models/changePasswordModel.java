package com.hotel.hotel_stars.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class changePasswordModel {
    String username;
    String password;
    String resetPassword;
    String confirmPassword;
    String token;
}
