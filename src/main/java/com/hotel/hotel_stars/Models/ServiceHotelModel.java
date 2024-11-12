package com.hotel.hotel_stars.Models;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceHotelModel {
    Integer id;

    @NotBlank(message = "Tên dịch vụ khách sạn không được để trống")
    @Size(max = 100, message = "Tên dịch vụ khách sạn không được vượt quá 100 ký tự")
    String serviceHotelName;

    @NotNull(message = "Giá không được để trống")
    @PositiveOrZero(message = "Giá phải là số dương hoặc 0")
    Double price;
    
    String image;
}