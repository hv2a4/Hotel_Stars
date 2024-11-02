package com.hotel.hotel_stars.Models;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImgageModel {
    @NotNull(message = "ID không được để trống")
    private Integer id;

    @NotNull(message = "Tên ảnh không được để trống")
    @Size(min = 2, max = 100, message = "Tên ảnh phải có độ dài từ 2 đến 100 ký tự")
    private String imageName;

}
