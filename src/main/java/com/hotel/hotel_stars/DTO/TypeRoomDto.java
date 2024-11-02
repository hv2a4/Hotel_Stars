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
    private Integer id;
    private String typeRoomName;
    private Double price;
    private TypeBedDto bedType;
    private Integer bedCount;
    private Double acreage;
    private Integer guestLimit;
}
