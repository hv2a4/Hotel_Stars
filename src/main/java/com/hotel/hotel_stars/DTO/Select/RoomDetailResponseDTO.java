package com.hotel.hotel_stars.DTO.Select;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDetailResponseDTO {
    private Integer roomId;
    private Integer typeRoomId;
    private String typeRoomName;
    private Double price;
    private Integer bedCount;
    private Double acreage;
    private Integer guestLimit;
    private String bedName;
    private String describes;
    private List<String> imageNames;
    private List<String> amenities;
    private Double finalPrice;
    private Double estCost;
    private Double percent;
}
