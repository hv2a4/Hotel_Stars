package com.hotel.hotel_stars.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.TypeRoom}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeRoomDto implements Serializable {
    Integer id;
    String typeRoomName;
    Double price;
    Integer bedCount;
    Double acreage;
    String guestLimit;
    TypeBedDto typeBedDto;
}
