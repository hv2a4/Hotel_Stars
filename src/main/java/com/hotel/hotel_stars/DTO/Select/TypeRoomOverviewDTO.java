package com.hotel.hotel_stars.DTO.Select;

import com.hotel.hotel_stars.DTO.TypeBedDto;
import com.hotel.hotel_stars.DTO.TypeRoomImageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeRoomOverviewDTO {
    Integer id;
    String typeRoomName;
    Integer roomCount;
    Double price;
    TypeBedDto typeBed;
    Integer bedCount;
    Integer guestLimit;
    Double acreage;
    TypeRoomImageDto typeRoomImage;
}
