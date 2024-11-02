package com.hotel.hotel_stars.Models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomModel {

    private Integer id;

    @NotBlank(message = "Tên phòng không được để trống")
    private String roomName;

    @NotNull(message = "ID tầng không được để trống")
    @Min(value = 1, message = "ID tầng phải lớn hơn 0")
    private Integer floorId;

    @NotNull(message = "ID loại phòng không được để trống")
    @Min(value = 1, message = "ID loại phòng phải lớn hơn 0")
    private Integer typeRoomId;

    @NotNull(message = "ID trạng thái phòng không được để trống")
    @Min(value = 1, message = "ID trạng thái phòng phải lớn hơn 0")
    private Integer statusRoomId;
}
