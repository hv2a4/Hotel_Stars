package com.hotel.hotel_stars.DTO.Select;

import java.time.Instant;

import com.hotel.hotel_stars.DTO.BookingDto;
import com.hotel.hotel_stars.DTO.RoomDto;

import lombok.Data;

@Data
public class BookingRoomAccountDto {
	Integer id;
    Instant checkIn;
    Instant checkOut;
    Double price;
    Boolean statusPayment;
    RoomDto room;
}
