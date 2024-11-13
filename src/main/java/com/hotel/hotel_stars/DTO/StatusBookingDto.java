package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.Booking;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.StatusBooking}
 */
@Value
public class StatusBookingDto implements Serializable {
    Integer id;
    String statusBookingName;
    //List<BookingDto> bookingList;
}