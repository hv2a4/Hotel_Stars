package com.hotel.hotel_stars.DTO;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.StatusRoom}
 */
@Value
public class StatusRoomDto implements Serializable {
    Integer id;
    String statusRoomName;
}