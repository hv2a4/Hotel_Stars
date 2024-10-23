package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.BookingRoom;
import com.hotel.hotel_stars.Entity.ServiceHotel;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.BookingRoomServiceHotel}
 */
@Value
public class BookingRoomServiceHotelDto implements Serializable {
    Integer id;
    Instant createAt;
    Double price;
    Integer quantity;
    BookingRoomDto bookingRoom;
    ServiceHotelDto serviceHotel;
}