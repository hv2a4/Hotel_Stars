package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.Account;
import com.hotel.hotel_stars.Entity.MethodPayment;
import com.hotel.hotel_stars.Entity.StatusBooking;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.Booking}
 */
@Value
public class BookingDto implements Serializable {
    Integer id;
    Instant createAt;
    Instant startAt;
    Instant endAt;
    Instant updateAt;
    Boolean statusPayment;
    MethodPaymentDto methodPaymentDto;
    AccountDto accountDto;
    StatusBookingDto statusBookingDto;
}