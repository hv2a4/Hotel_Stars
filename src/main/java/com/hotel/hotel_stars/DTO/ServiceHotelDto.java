package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.BookingRoomServiceHotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.ServiceHotel}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceHotelDto implements Serializable {
    Integer id;
    String serviceHotelName;
    Double price;
    HotelDto hotel;
    String image;
}