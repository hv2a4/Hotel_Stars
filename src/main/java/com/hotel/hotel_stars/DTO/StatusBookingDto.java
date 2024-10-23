package com.hotel.hotel_stars.DTO;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.StatusBooking}
 */
@Value
public class StatusBookingDto implements Serializable {
    Integer id;
    String statusBookingName;
}