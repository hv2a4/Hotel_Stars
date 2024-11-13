package com.hotel.hotel_stars.DTO.Select;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationInfoDTO {
    private Integer bookingId;
    private Integer accountId;
    private Integer statusBookingId;
    private Integer methodPaymentId;
    private Integer bookingRoomId;
    private Integer roomId;
    private Integer typeRoomId;
    private Integer invoiceId;
    private String roomName;
    private String methodPaymentName;
    private String statusRoomName;
    private String statusBookingName;
    private LocalDateTime createAt;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String accountFullname;
    private String roleName;
    private String typeRoomName;
    private Double total_amount;
    private Integer max_guests;
}
