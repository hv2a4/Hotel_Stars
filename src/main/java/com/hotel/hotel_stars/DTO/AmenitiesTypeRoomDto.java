package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.TypeRoomAmenitiesTypeRoom;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.AmenitiesTypeRoom}
 */
@Value
public class AmenitiesTypeRoomDto implements Serializable {
    Integer id;
    String amenitiesTypeRoomName;
}