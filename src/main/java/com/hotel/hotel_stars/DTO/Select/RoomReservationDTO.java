package com.hotel.hotel_stars.DTO.Select;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomReservationDTO {
    private String roomName;
    private String typeRoomName;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String guestName;
    private String statusRoomName;
}
