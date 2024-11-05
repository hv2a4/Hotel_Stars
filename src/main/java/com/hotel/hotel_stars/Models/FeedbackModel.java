package com.hotel.hotel_stars.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackModel {
    Integer id;
    String content;
    Integer stars;
    Instant createAt;
    Boolean Status;
    Integer IdInvoce;
}
