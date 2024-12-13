package com.hotel.hotel_stars.DTO.Select;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomAvailabilityInfo {
    private List<Integer> roomId;                   // ID của phòng
    private List<String> roomName;                  // Tên phòng
    private Integer typeRoomId;               // ID loại phòng
    private String typeRoomName;              // Tên loại phòng
    private Double price;                     // Giá phòng
    private Double acreage;                   // Diện tích phòng
    private Integer guestLimit;               // Số lượng khách tối đa
    private List<String> amenitiesDetails;          // Chi tiết tiện ích phòng
    private List<String> imageList;           // Danh sách hình ảnh (dưới dạng mảng)
    private String description;               // Mô tả phòng
    private List<String> bedNames;            // Tên giường (dưới dạng mảng)
    private List<Integer> amenitiesIds;       // Danh sách ID tiện ích (dưới dạng mảng)
    private Double finalPrice;
    private Double estCost;
    private Double percent;
}
