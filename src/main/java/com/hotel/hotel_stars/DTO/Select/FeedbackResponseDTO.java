package com.hotel.hotel_stars.DTO.Select;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponseDTO {
    private String fullName;
    private String Content;
    private String avatar;
}
