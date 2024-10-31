package com.hotel.hotel_stars.DTO.Select;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo {
    private Integer id;
    private String username;
    private String passwords;
    private String fullname;
    private String email;
    private String avatar;
    private Boolean gender;
    private String phone;
}
