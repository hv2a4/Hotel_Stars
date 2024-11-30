package com.hotel.hotel_stars.Models;

import java.time.Instant;

import com.hotel.hotel_stars.Entity.BookingRoom;
import com.hotel.hotel_stars.Entity.ServiceRoom;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRoomServiceRoomModel {
    Instant createAt;
    Double price;
    Integer quantity;
    Integer bookingRoomId;
    Integer serviceRoomId;
}
