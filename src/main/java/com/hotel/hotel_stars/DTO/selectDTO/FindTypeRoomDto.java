package com.hotel.hotel_stars.DTO.selectDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindTypeRoomDto {
    List<Integer> roomId;
    List<String> roomName;
    Integer typeRoomId;
    String typeRoomName;
    Double price;
    Double acreage;
    Integer guestLimit;
    List<String> amenitiesDetails;
    Double estCost;
    List<String> imageList;
    String describes;
    List<String> bedNames;
}
