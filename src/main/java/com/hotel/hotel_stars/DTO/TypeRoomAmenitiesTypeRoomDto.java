package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.AmenitiesTypeRoom;
import com.hotel.hotel_stars.Entity.TypeRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.TypeRoomAmenitiesTypeRoom}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeRoomAmenitiesTypeRoomDto implements Serializable {
    Integer id;
    TypeRoomDto typeRoomDto;
    AmenitiesTypeRoomDto amenitiesTypeRoomDto;
}