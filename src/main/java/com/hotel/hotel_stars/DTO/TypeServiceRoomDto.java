package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.ServiceRoom;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

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
    String duration;
    Set<ServiceRoomDto> serviceRoomDtos;
}