package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.TypeServiceHotel;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.ServiceHotel}
 */
@Value
public class ServiceHotelDto implements Serializable {
    Integer id;
    String serviceHotelName;
    Double price;
    TypeServiceHotelDto typeServiceHotelDto;
}