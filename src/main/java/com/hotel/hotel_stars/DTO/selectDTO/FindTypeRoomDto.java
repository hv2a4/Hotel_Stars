package com.hotel.hotel_stars.DTO.selectDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindTypeRoomDto {
    Integer roomId;
    String roomName;
    Integer roomTypeId;
    String roomTypeName;
    Double priceTypeRoom;
    Double acreage;
    Integer guestLimit;
    String amenitiesTypeRoomDetails;
    Double estCost;
    List<String> listImages;
    String describes;


}
