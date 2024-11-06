package com.hotel.hotel_stars.DTO.Select;

import com.hotel.hotel_stars.DTO.TypeBedDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeRoomBookingCountDto {
    private Integer typeRoomBookingCount; // Số lần đặt
    private Integer id; // ID loại phòng
    private String typeRoomName; // Tên loại phòng
    private Double price; // Giá
    private Integer bedCount; // Số giường
    private Double acreage; // Diện tích
    private String guestLimit; // Giới hạn khách
    private String typeBedDto; // Tên loại giường
    private Double averageStars; // Đánh giá trung bình
    private String[] amenities; // Tiện nghi
    private String[] amenitiesIcon; // Biểu tượng tiện nghi
}
