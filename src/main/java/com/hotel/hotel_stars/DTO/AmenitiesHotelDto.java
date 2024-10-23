package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.TypeAmenitiesHotel;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.AmenitiesHotel}
 */
@Value
public class AmenitiesHotelDto implements Serializable {
    Integer id;
    String amenitiesHotelName;
    TypeAmenitiesHotelDto typeAmenitiesHotelDto;
}