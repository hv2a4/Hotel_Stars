package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.Account;
import com.hotel.hotel_stars.Entity.BookingRoom;
import com.hotel.hotel_stars.Entity.MethodPayment;
import com.hotel.hotel_stars.Entity.StatusBooking;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.Booking}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto implements Serializable {
    Integer id;
    LocalDateTime createAt;
    Instant startAt;
    Instant endAt;
    Boolean statusPayment;
    String descriptions;
    StatusBookingDto statusDto;
    AccountDto accountDto;
    MethodPaymentDto methodPaymentDto;
    String discountName;
    Double discountPercent;
    //List<BookingRoomDto> bookingRooms;
}