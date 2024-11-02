package com.hotel.hotel_stars.Models;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicePackageModel {
    Integer id;

    @NotBlank(message = "Tên gói dịch vụ không được để trống")
    String servicePackageName;

    @NotNull(message = "Giá không được để trống")
    @Positive(message = "Giá phải lớn hơn 0")
    Double price;
}
