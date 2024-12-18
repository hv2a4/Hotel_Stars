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
    private String content;
    private Integer stars;
    private Integer idInvoice;
    private Instant createAt;
    private Boolean ratingStatus;

    private Integer IdInvoice;
}
