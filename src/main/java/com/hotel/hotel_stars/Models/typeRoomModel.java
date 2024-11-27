package com.hotel.hotel_stars.Models;

import com.hotel.hotel_stars.DTO.AmenitiesTypeRoomDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Data
@Getter
@Setter
public class typeRoomModel {
    private Integer id;

    @NotBlank(message = "Tên loại phòng không được để trống")
    private String typeRoomName;

    @NotNull(message = "Giá loại phòng không được để trống")
    @Positive(message = "Giá phải lớn hơn 0")
    private Double price;

    private Integer typeBedId;

    @NotNull(message = "Số lượng giường không được để trống")
    @Positive(message = "Số lượng giường phải lớn hơn 0")
    private Integer bedCount;

    @NotNull(message = "Diện tích loại phòng không được để trống")
    @Positive(message = "Diện tích loại phòng phải lớn hơn 0")
    private Double acreage;

    @NotNull(message = "Sức chứa không được để trống")
    @Positive(message = "Sức chứa phải lớn hơn 0")
    private Integer guestLimit;

    private String describes;

    private String[] imageNames;

    private List<amenitiesTypeRoomModel> amenitiesTypeRooms;
}
