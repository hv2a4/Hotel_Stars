package com.hotel.hotel_stars.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

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
}