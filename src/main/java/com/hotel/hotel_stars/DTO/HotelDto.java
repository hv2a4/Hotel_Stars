package com.hotel.hotel_stars.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.Hotel}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDto implements Serializable {
    Integer id;
    String hotelName;
    String descriptions;
    String province;
    String district;
    String ward;
    String address;
    String hotelPhone;
}