package com.hotel.hotel_stars.DTO.Select;

import com.hotel.hotel_stars.DTO.TypeRoomDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomRevenueDTO {
    private TypeRoomDto typeRoomId;
    private String typeRoomName;
    private Double revenue;
    private Long bookingCount;
    private Double avgRevenuePerBooking;
    private Double avgDiscountPercent;
    private Double revenuePercentage;
}
