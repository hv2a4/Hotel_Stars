package com.hotel.hotel_stars.DTO;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.AmenitiesTypeRoom}
 */
@Value
public class AmenitiesTypeRoomDto implements Serializable {
    Integer id;
    String amenitiesTypeRoomName;
    String icon;
}