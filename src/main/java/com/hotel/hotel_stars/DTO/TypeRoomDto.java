package com.hotel.hotel_stars.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

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
    String bedType;
    Integer bedCount;
    Double acreage;
    Integer guestLimit;
}
