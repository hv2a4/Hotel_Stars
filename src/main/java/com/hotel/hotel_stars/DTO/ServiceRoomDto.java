package com.hotel.hotel_stars.DTO;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.ServiceRoom}
 */
@Value
public class ServiceRoomDto implements Serializable {
    Integer id;
    String serviceRoomName;
    Double price;
    TypeServiceRoomDto typeServiceRoomDto;
    String imageName;
}