package com.hotel.hotel_stars.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeRoomWithReviewsDTO {
    private Integer id;
    private String typeRoomName;
    private Double price;
    private Integer bedCount;
    private Double acreage;
    private Integer guestLimit;
    private String describes;
    private List<TypeRoomImageDto> imageId;
    private List<TypeRoomAmenitiesTypeRoomDto> amenitiesId;
    private Long totalReviews;
    private Double averageStars;
}
