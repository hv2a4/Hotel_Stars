package com.hotel.hotel_stars.DTO;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.Account}
 */
@Value
public class AccountDto implements Serializable {
    Integer id;
    String username;
    String fullname;
    String phone;
    String email;
    String avatar;
    Boolean gender;
    Boolean isDelete;
    RoleDto roleDto;
}