package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.ServicePackage;
import com.hotel.hotel_stars.Entity.TypeRoom;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.TypeRoomServicePackage}
 */
@Value
public class TypeRoomServicePackageDto implements Serializable {
    Integer id;
    TypeRoomDto typeRoomDto;
    ServicePackageDto servicePackageDto;
}