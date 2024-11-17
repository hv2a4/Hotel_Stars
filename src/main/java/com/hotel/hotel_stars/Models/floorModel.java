package com.hotel.hotel_stars.Models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class floorModel {
    private Integer id;

    @NotBlank(message = "Tên tầng không được để trống")
    private String floorName;
}
