package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.Account;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.Rating}
 */
@Value
public class RatingDto implements Serializable {
    Integer id;
    String content;
    Integer stars;
    Instant createAt;
    Boolean ratingStatus;
    AccountDto accountDto;
}