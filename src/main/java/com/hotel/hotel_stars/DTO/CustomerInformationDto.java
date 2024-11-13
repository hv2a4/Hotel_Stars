package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.BookingRoomCustomerInformation;
import com.hotel.hotel_stars.Entity.BookingRoomServiceRoom;
import jakarta.persistence.OneToMany;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.CustomerInformation}
 */
@Value
public class CustomerInformationDto implements Serializable {
    Integer id;
    String cccd;
    String fullname;
    String phone;
    Boolean gender;
    Instant birthday;
    String imgFirstCard;
    String imgLastCard;
//    List<BookingRoomServiceRoomDto> serviceRooms;
//    List<BookingRoomCustomerInformationDto> customerInformationList;
//    List<BookingRoomServiceRoomDto> serviceRoomList;
}