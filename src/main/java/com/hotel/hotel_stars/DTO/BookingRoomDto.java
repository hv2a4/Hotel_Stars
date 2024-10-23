package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.Booking;
import com.hotel.hotel_stars.Entity.Room;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.BookingRoom}
 */
@Value
public class BookingRoomDto implements Serializable {
    Integer id;
    Instant checkIn;
    Instant checkOut;
    Double price;
    Boolean statusPayment;
    Booking booking;
    Room room;
}