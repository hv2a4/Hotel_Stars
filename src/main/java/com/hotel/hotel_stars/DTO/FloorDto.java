package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.Hotel;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.Floor}
 */
@Value
public class FloorDto implements Serializable {
    Integer id;
    String floorName;
    HotelDto hotelDto;
}