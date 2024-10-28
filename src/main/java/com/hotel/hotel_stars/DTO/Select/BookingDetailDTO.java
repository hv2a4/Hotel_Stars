package com.hotel.hotel_stars.DTO.Select;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetailDTO {
    private Integer bookingId;
    private String typeRoomName;
    private String roomName;
    private Instant checkIn;
    private Instant checkOut;
    private Integer numberOfDays;

}
