package com.hotel.hotel_stars.DTO;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.TypeServiceRoom}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeServiceRoomDto implements Serializable {
    Integer id;
    @Size(max = 255)
    String serviceRoomName;
}