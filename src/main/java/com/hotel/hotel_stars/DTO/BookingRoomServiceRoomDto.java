package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.BookingRoom;
import com.hotel.hotel_stars.Entity.ServiceRoom;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.BookingRoomServiceRoom}
 */
@Value
public class BookingRoomServiceRoomDto implements Serializable {
    Integer id;
    Instant createAt;
    Double price;
    Integer quantity;
    BookingRoomDto bookingRoomDto;
    ServiceRoomDto serviceRoomDto;
}