package com.hotel.hotel_stars.Models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class amenitiesTypeRoomModel {
    private Integer id;

    @NotBlank(message = "Tên loại tiện nghi phòng không được để trống")
    private String amenitiesTypeRoomName;

//    private String icon;
}
