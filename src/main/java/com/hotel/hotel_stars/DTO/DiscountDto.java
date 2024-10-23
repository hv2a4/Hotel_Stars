package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.TypeRoom;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.Discount}
 */
@Value
public class DiscountDto implements Serializable {
    Integer id;
    String discountName;
    Double percent;
    Instant startDate;
    Instant endDate;
    TypeRoomDto typeRoomDto;

}