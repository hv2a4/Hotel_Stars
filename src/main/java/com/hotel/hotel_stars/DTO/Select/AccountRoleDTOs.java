package com.hotel.hotel_stars.DTO.Select;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRoleDTOs {
    private Integer id;
    private String fullname;
    private String avatar;
    private String roleName;
}
