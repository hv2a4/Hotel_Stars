package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.Hotel;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.TypeServiceHotel}
 */
@Value
public class TypeServiceHotelDto implements Serializable {
    Integer id;
    String typeServiceHotelName;
    HotelDto hotelDto;
}