package com.hotel.hotel_stars.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeleteBookingModel {
    Integer bookingId;
    String descriptions;
}
