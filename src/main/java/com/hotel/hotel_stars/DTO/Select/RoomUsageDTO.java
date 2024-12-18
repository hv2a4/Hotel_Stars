package com.hotel.hotel_stars.DTO.Select;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomUsageDTO {
    private Long occupiedRooms;
    private Long totalRooms;
    private Double usagePercentage;
}
