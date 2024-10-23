package com.hotel.hotel_stars.DTO;

import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.CustomerInformation}
 */
@Value
public class CustomerInformationDto implements Serializable {
    Integer id;
    String cccd;
    String fullname;
    String phone;
    Boolean gender;
    Instant birthday;
}