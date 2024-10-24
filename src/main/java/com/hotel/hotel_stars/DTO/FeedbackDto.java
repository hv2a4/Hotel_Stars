package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.Feedback;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link Feedback}
 */
@Value
public class FeedbackDto implements Serializable {
    Integer id;
    String content;
    Integer stars;
    Instant createAt;
    Boolean ratingStatus;
    AccountDto accountDto;
}