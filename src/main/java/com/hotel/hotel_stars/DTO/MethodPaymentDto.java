package com.hotel.hotel_stars.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.MethodPayment}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MethodPaymentDto implements Serializable {
    Integer id;
    String methodPaymentName;
}