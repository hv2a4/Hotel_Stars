package com.hotel.hotel_stars.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeRoomImageModel {
    Integer id;
    String imageName;
    Integer typeRoom_Id;
}
