package com.hotel.hotel_stars.DTO.Select;

import com.hotel.hotel_stars.DTO.AmenitiesTypeRoomDto;
import com.hotel.hotel_stars.DTO.FeedbackDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypeDetail {
    private Integer typeRoomId;
    private String typeRoomName;
    private Double price;           // Changed from Integer to Double
    private Integer bedCount;
    private Double acreage;
    private Integer guestLimit;
    private String describes;
    private String bedName;
    private List<String> imageList;
    private List<AmenitiesTypeRoomDto> amenitiesList;
    private List<FeedbackDto> feedBack;
    private Double averageFeedBack;
    private List<String> accountNames;
    private List<String> Image;
}
