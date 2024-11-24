package com.hotel.hotel_stars.Models;

import java.time.Instant;
import java.util.List;

import com.hotel.hotel_stars.DTO.AccountDto;
import com.hotel.hotel_stars.DTO.BookingDto;
import com.hotel.hotel_stars.DTO.RoomDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class bookingRoomModel {
	Integer id;
    Instant checkIn;
    Instant checkOut;
    Double price;
    Boolean statusPayment;
    Integer bookingId;
    Integer roomId;
}
