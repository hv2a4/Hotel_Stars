 package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.*;
import jakarta.persistence.OneToMany;
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
    Integer bedCount;
    Double acreage;
    Integer guestLimit;
    TypeBedDto typeBedDto;
    String describes;
    List<TypeRoomImageDto> typeRoomImageDto;
}
