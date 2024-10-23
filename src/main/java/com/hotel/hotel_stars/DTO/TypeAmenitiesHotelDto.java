package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.Hotel;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.TypeAmenitiesHotel}
 */
@Value
public class TypeAmenitiesHotelDto implements Serializable {
    Integer id;
    String typeAmenitiesHotelName;
    HotelDto hotelDto;
}