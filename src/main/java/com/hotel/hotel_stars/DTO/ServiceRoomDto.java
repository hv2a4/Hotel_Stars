package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.BookingRoomServiceRoom;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

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
    //List<BookingRoomServiceRoomDto> bookingRoomServiceRooms;
}