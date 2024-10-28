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
    private Integer capacity;
    private List<String> amenitiesTypeRoomNames;
    private Double estCost;

    public FindTypeRoomDto(String typeRoomName, Double acreage, Integer capacity, List<String> amenitiesTypeRoomNames, Double estCost) {
        this.typeRoomName = typeRoomName;
        this.acreage = acreage;
        this.capacity = capacity;
        this.amenitiesTypeRoomNames = amenitiesTypeRoomNames;
        this.estCost = estCost;
    }


}
