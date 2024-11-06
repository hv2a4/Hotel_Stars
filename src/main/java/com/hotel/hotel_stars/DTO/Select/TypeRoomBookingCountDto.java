package com.hotel.hotel_stars.DTO.Select;

import com.hotel.hotel_stars.DTO.TypeBedDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeRoomBookingCountDto {
    private Integer typeRoomBookingCount;
    private Integer id;
    private String typeRoomName;
    private Double price;
    private Integer bedCount;
    private Double acreage;
    private String guestLimit;
    private String typeBedDto;
    private Double averageStars;

}
