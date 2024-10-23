package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.TypeRoom;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.TypeRoomImage}
 */
@Value
public class TypeRoomImageDto implements Serializable {
    Integer id;
    String imageName;
    TypeRoomDto typeRoomDto;
}