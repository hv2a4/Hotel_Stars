package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.Hotel;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.HotelImage}
 */
@Value
public class HotelImageDto implements Serializable {
    Integer id;
    String imageName;
    HotelDto hotelDto;
}