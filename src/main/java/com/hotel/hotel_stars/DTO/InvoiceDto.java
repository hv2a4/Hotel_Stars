package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.Booking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.Invoice}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDto implements Serializable {
    Integer id;
    Instant createAt;
    Boolean invoiceStatus;
    Double totalAmount;
    BookingDto bookingDto;
}