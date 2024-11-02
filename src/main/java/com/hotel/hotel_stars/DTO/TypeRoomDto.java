package com.hotel.hotel_stars.DTO;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.TypeRoom}
 */
@Value
public class TypeRoomDto implements Serializable {
    Integer id;
    String typeRoomName;
    Double price;
    String bedType;
    Integer bedCount;
    Double acreage;
    Integer guestLimit;
}