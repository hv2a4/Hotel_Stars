package com.hotel.hotel_stars.DTO.Select;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerReservation {
    Integer accountId;
    String fullname;
    String phone;
    String email;
    Integer bookingId;
    Instant startAt;
    Instant endAt;
    Integer maxGuests;
    String typeRoomName;
    String roomName;
    Double price;
    Double totalAmount;
    String statusBookingName;
    String methodPaymentName;
    Boolean statusPayment;
    String roleName;
}
