package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.Booking;
import com.hotel.hotel_stars.Entity.Rating;
import com.hotel.hotel_stars.Entity.Role;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

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
    RoleDto roleDto;
}