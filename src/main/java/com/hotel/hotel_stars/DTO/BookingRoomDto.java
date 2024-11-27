package com.hotel.hotel_stars.DTO;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hotel.hotel_stars.Entity.Booking;
import com.hotel.hotel_stars.Entity.Room;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.BookingRoom}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRoomDto implements Serializable {
    Integer id;
    Instant checkIn;
    Instant checkOut;
    Double price;
    BookingDto booking;
    RoomDto room;
    AccountDto accountDto;
}