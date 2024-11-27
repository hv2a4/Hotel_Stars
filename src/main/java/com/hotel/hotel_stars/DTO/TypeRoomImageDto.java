package com.hotel.hotel_stars.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.TypeRoomImage}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeRoomImageDto implements Serializable {
    Integer id;
    String imageName;
    
    @JsonIgnore
    TypeRoomDto typeRoomDto;
}