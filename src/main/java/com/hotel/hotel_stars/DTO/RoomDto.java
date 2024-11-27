package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.Floor;
import com.hotel.hotel_stars.Entity.StatusRoom;
import com.hotel.hotel_stars.Entity.TypeRoom;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.Room}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto implements Serializable {
    Integer id;
    String roomName;
    FloorDto floorDto;
    TypeRoomDto typeRoomDto;
    StatusRoomDto statusRoomDto;
}