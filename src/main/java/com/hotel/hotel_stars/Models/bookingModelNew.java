package com.hotel.hotel_stars.Models;

import java.time.Instant;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class bookingModelNew {
//    @NotBlank(message = "Không được bỏ trống username")
    String userName;

    @NotBlank(message = "Không được bỏ trống ngày bắt đầu")
    Instant startDate;
    @NotBlank(message = "Không được bỏ trống ngày kết thúc")
    Instant endDate;

    String discountName;

    Double discountPercent;

    @NotEmpty(message = "Không được bỏ trống id phòng")
    List<Integer> roomId;
}