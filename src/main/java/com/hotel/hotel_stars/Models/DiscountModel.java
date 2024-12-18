package com.hotel.hotel_stars.Models;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountModel {

    Integer id;

    @NotBlank(message = "Tên giảm giá không được để trống.")
    @Size(max = 100, message = "Tên giảm giá không được vượt quá 100 ký tự.")
    String discountName;

    @NotNull(message = "Phần trăm giảm giá không được để trống.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Phần trăm giảm giá phải lớn hơn 0.")
    @DecimalMax(value = "100.0", message = "Phần trăm giảm giá không được vượt quá 100.")
    Double percent;

    @NotNull(message = "Ngày bắt đầu không được để trống.")
    Instant startDate;

    @NotNull(message = "Ngày kết thúc không được để trống.")
    Instant endDate;


}
