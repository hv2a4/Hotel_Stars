package com.hotel.hotel_stars.Models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class amenitiesHotelModel {
    private Integer id;

    @NotBlank(message = "Tên tiện nghi khách sạn không được để trống")
    private String amenitiesHotelName;
}
