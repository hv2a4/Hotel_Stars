package com.hotel.hotel_stars.DTO.selectDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class bookingHistoryDTO {
    private Integer bkId;
    private String bkFormat;
    private String createAt;
    private String startAt;
    private String endAt;
    private Integer ivId;
    private Double total;
    private Integer fbId;
    private String content;
    private Integer stars;
    private String roomName;
    private String trName;
    private String image;
}
