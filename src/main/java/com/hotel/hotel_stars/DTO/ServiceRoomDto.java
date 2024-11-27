package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.BookingRoomServiceRoom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.ServiceRoom}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRoomDto implements Serializable {
    Integer id;
    String serviceRoomName;
    Double price;
    TypeServiceRoomDto typeServiceRoomDto;
    String imageName;
    List<BookingRoomServiceRoomDto> bookingRoomServiceRooms;
}