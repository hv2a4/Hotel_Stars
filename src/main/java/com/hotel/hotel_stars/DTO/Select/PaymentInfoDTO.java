package com.hotel.hotel_stars.DTO.Select;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfoDTO {
    private String methodPaymentName;
    private Boolean statusPayment;
    private Double totalAmount;
}
