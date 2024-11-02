package com.hotel.hotel_stars.DTO.selectDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FindTypeRoomDto {
    private String typeRoomName;
    private Double acreage;
    private Double price;
    private Integer guestLimit;
    private List<String> amenitiesTypeRoomNames;
    private Double estCost;
    private String image;
    public FindTypeRoomDto(String typeRoomName,  Double price,Double acreage, Integer guestLimit, List<String> amenitiesTypeRoomNames, Double estCost
    ,String image) {
        this.typeRoomName = typeRoomName;
        this.price = price;
        this.acreage = acreage;
        this.guestLimit = guestLimit;
        this.amenitiesTypeRoomNames = amenitiesTypeRoomNames;
        this.estCost = estCost;
        this.image = image;
    }


}
