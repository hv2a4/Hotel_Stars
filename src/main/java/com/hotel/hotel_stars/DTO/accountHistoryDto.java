package com.hotel.hotel_stars.DTO;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import com.hotel.hotel_stars.DTO.Select.AccountInfo;
import com.hotel.hotel_stars.DTO.Select.BookingRoomAccountDto;
import lombok.Data;

@Data
public class accountHistoryDto {
    Integer id;
    LocalDateTime createAt;
    Instant startAt;
    Instant endAt;
    Boolean statusPayment;
    AccountInfo accountDto;
    String descriptions;
    StatusBookingDto statusBookingDto;
    MethodPaymentDto methodPaymentDto;
    List<BookingRoomDto> bookingRooms;
    List<InvoiceDto> invoiceDtos;
    String disCountName;
    Double discountPercent;
}
