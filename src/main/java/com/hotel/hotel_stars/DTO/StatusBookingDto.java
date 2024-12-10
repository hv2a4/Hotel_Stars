package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.Booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.StatusBooking}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusBookingDto implements Serializable {
    Integer id;
    String statusBookingName;
    //List<BookingDto> bookingList;
}