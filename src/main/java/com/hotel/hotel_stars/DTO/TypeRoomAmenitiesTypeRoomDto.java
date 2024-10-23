package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.AmenitiesTypeRoom;
import com.hotel.hotel_stars.Entity.TypeRoom;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.TypeRoomAmenitiesTypeRoom}
 */
@Value
public class TypeRoomAmenitiesTypeRoomDto implements Serializable {
    Integer id;
    TypeRoomDto typeRoomDto;
    AmenitiesTypeRoomDto amenitiesTypeRoomDto;
}