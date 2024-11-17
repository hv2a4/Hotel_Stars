package com.hotel.hotel_stars.DTO;

import java.time.Instant;
import java.util.List;

import com.hotel.hotel_stars.DTO.Select.AccountInfo;
import com.hotel.hotel_stars.DTO.Select.BookingRoomAccountDto;
import lombok.Data;

@Data
public class accountHistoryDto {
	Integer id;
    Instant createAt;
    Instant startAt;
    Instant endAt;
    Boolean statusPayment;
    AccountInfo accountDto;
    MethodPaymentDto methodPaymentDto;
    List<BookingRoomDto> bookingRooms;
    List<InvoiceDto> invoiceDtos;
}
