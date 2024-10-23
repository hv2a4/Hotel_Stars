package com.hotel.hotel_stars.DTO;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.Role}
 */
@Value
public class RoleDto implements Serializable {
    Integer id;
    String roleName;
}