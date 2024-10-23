package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.Floor;
import com.hotel.hotel_stars.Entity.StatusRoom;
import com.hotel.hotel_stars.Entity.TypeRoom;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.Room}
 */
@Value
public class RoomDto implements Serializable {
    Integer id;
    String roomName;
    FloorDto floorDto;
    TypeRoomDto typeRoomDto;
    StatusRoomDto statusRoomDto;

}