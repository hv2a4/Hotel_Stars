package com.hotel.hotel_stars.DTO.Select;

import com.hotel.hotel_stars.DTO.TypeRoomImageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeRoomOverviewDTO {
    Integer typeId;
    String typeName;
    Integer roomCount;
    Double price;
    Integer typeBed;
    String guestLimit;
    Double acreage;
    TypeRoomImageDto imageId;
}
