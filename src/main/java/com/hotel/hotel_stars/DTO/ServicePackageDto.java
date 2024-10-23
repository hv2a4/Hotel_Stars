package com.hotel.hotel_stars.DTO;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.ServicePackage}
 */
@Value
public class ServicePackageDto implements Serializable {
    Integer id;
    String servicePackageName;
    Double price;
}