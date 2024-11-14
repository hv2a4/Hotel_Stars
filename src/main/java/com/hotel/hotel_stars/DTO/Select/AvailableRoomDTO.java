package com.hotel.hotel_stars.DTO.Select;

import com.hotel.hotel_stars.DTO.TypeRoomAmenitiesTypeRoomDto;
import com.hotel.hotel_stars.DTO.TypeRoomImageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableRoomDTO {
    private Integer roomId;
    private String roomName;
    private String typeRoomName;
    private Integer guestLimit;
    private Integer bedCount;
    private Double area;
    private String description;
    private List<TypeRoomImageDto> images;
    private String roomStatus;
    private Integer roomTypeId;
    private Double price;
    private List<TypeRoomAmenitiesTypeRoomDto> amenities;
}
