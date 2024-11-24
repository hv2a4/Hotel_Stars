package com.hotel.hotel_stars.DTO.Select;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomListBooking {
    private Integer roomId;
    private String roomName;
    private Integer floorId;
    private Integer typeRoomId;
    private String typeRoomName;
    private Double price;
    private Integer bedCount;
    private Double acreage;
    private Integer guestLimit;
    private String describes;
    private List<String> listImageName;
}
