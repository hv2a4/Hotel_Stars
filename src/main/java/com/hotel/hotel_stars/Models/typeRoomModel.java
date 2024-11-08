package com.hotel.hotel_stars.Models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


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

//    @NotBlank(message = "Tên loại giường không được để trống")
    private Integer typeBedId;

    @NotNull(message = "Số lượng giường không được để trống")
    @Positive(message = "Số lượng giường phải lớn hơn 0")
    private Integer bedCount;

    @NotNull(message = "Diện tích loại phòng không được để trống")
    @Positive(message = "Diện tích loại phòng phải lớn hơn 0")
    private Double acreage;

    @NotBlank(message = "Giới hạn khách không được để trống")
    private String guestLimit;
}
