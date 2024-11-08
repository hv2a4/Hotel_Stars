package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.TypeRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeBedDto {
    private Integer id;
    private String bedName;
    //private List<TypeRoomDto> typeRooms;
}
