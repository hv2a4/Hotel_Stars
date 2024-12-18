package com.hotel.hotel_stars.Models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class bookingModel {
    //    @NotBlank(message = "Không được bỏ trống username")
    String userName;

    @NotBlank(message = "Không được bỏ trống ngày bắt đầu")
    String startDate;
    @NotBlank(message = "Không được bỏ trống ngày kết thúc")
    String endDate;

    String discountName;
    Integer methodPayment;
    @NotEmpty(message = "Không được bỏ trống id phòng")
    List<Integer> roomId;
}
