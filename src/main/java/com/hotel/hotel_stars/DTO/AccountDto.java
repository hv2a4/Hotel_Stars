package com.hotel.hotel_stars.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.Account}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    List<BookingDto> bookingDtoList;
}