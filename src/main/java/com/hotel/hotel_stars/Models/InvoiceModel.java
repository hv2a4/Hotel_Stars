package com.hotel.hotel_stars.Models;

import java.time.Instant;

import com.hotel.hotel_stars.DTO.BookingDto;

import lombok.Data;

@Data
public class InvoiceModel {
	Integer id;
    Instant createAt;
    Boolean invoiceStatus;
    Double totalAmount;
    Integer bookingId;
}
