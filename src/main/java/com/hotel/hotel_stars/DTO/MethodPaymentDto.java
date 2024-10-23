package com.hotel.hotel_stars.DTO;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.MethodPayment}
 */
@Value
public class MethodPaymentDto implements Serializable {
    Integer id;
    String methodPaymentName;
}