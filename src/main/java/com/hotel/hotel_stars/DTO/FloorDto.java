package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.Hotel;
import com.hotel.hotel_stars.Entity.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.Floor}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FloorDto implements Serializable {
    Integer id;
    String floorName;
    List<RoomDto> roomDtos;
}