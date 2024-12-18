package com.hotel.hotel_stars.DTO.selectDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingHistoryDTOs {
    private Integer bkId;
    private String bkFormat;
    private String createAt;
    private String startAt;
    private String endAt;
    private String fullname;
    private String avatar;
    private Integer  statusBkID;
    private String statusBkName;
    private Integer ivId;
    private Double totalRoom;
    private Integer fbId;
    private String content;
    private Integer stars;
    private String roomInfo;
    private String image;
    private String combinedServiceNames;
    private Double combinedTotalServices;
    private Double totalBooking;
    private Integer methodPaymentId;
    private String methodPaymentName;
    private String discountName;
    private Double discountPercent;
    private Integer statusBookingID;
}
