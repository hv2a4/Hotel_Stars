package com.hotel.hotel_stars.Models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class statusRoomModel {
    private Integer id;

    @NotBlank(message = "Không đươc để trống trạng thái phòng")
    private String statusRoomName;
}
