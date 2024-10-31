package com.hotel.hotel_stars.DTO.Select;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBookingDTO {
    private String username;
    private String fullname;
    private String phoneNumber;
    private String email;
    private String role;
    private String serviceName;
    private LocalDateTime bookingCreationDate;
    private String AvtImage;
    private Boolean Gender;
    private Integer Id;
}
