package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.BookingRoom;
import com.hotel.hotel_stars.Entity.ServiceRoom;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.BookingRoomServiceRoom}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRoomServiceRoomDto implements Serializable {
    Integer id;
    Instant createAt;
    Double price;
    Integer quantity;
    BookingRoomDto bookingRoomDto;
    ServiceRoomDto serviceRoomDto;
}