package com.hotel.hotel_stars.DTO.Select;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import com.hotel.hotel_stars.DTO.AccountDto;
import com.hotel.hotel_stars.DTO.BookingDto;
import com.hotel.hotel_stars.DTO.BookingRoomDto;
import com.hotel.hotel_stars.DTO.MethodPaymentDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingSelectDto {
	Integer id;
    LocalDateTime createAt;
    Instant startAt;
    Instant endAt;
    Boolean statusPayment;
    AccountDto accountDto;
    MethodPaymentDto methodPaymentDto;
    List<BookingRoomDto> bookingRooms;
}
