package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.HotelImage}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelImageDto implements Serializable {
    Integer id;
    String imageName;
    HotelDto hotelDto;
}