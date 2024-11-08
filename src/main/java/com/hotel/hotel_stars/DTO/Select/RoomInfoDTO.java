package com.hotel.hotel_stars.DTO.Select;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomInfoDTO {
    private String roomName;
    private String typeRoomName;
    private String floorName;
    private String statusRoomName;
    private Integer roomId;
    private Integer typeRoomId;
    private Integer statusRoomId;
}
