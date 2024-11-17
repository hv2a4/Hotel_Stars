package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.BookingRoom;
import com.hotel.hotel_stars.Entity.CustomerInformation;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.BookingRoomCustomerInformation}
 */
@Data
public class BookingRoomCustomerInformationDto implements Serializable {
    Integer id;
    BookingRoomDto bookingRoomDto;
    CustomerInformationDto customerInformationDto;
}