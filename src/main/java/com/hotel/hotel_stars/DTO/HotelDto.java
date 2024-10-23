package com.hotel.hotel_stars.DTO;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.Hotel}
 */
@Value
public class HotelDto implements Serializable {
    Integer id;
    String hotelName;
    String descriptions;
    String province;
    String district;
    String ward;
    String address;
}