package com.hotel.hotel_stars.DTO.selectDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomAvailabilityResponse {
    private boolean allRoomsAvailable;
    private Integer unavailableRoomId;
}
