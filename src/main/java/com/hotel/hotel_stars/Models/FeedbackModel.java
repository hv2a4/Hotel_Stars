package com.hotel.hotel_stars.Models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackModel {
    private Integer id;

    @NotBlank(message = "Nội dung không được để trống")
    private String content;

    @NotNull(message = "Số sao không được để trống")
    @Positive(message = "Số bạn nhập lớn hơn 0")
    private Integer stars;

    private Instant createAt;

    @NotNull(message = "Không được để trống trạng thái")
    private Boolean ratingStatus;

    private Integer IdInvoice;
}
