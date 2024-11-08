package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.TypeRoomServicePackage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.ServicePackage}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicePackageDto implements Serializable {
    Integer id;
    String servicePackageName;
    Double price;
   // private List<TypeRoomServicePackageDto> roomServicePackageList;
}