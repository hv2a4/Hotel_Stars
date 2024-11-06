package com.hotel.hotel_stars.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class bookingModel {
    String username;
    String startDate;
    String endDatel;
    Integer quantityRoom;
    Integer idTypeRoom;
}
