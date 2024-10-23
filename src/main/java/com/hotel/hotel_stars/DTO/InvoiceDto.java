package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.Booking;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.Invoice}
 */
@Value
public class InvoiceDto implements Serializable {
    Integer id;
    Instant createAt;
    Boolean invoiceStatus;
    Double totalAmount;
    BookingDto bookingDto;
}