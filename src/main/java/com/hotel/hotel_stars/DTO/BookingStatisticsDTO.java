package com.hotel.hotel_stars.DTO;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingStatisticsDTO implements Serializable{
	private LocalDate bookingDate;
	private Long totalBookings;
	private Long totalRoomsBooked;
	private Double totalBookingValue;
	private Double totalPaid;
}
