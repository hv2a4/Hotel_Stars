package com.hotel.hotel_stars.DTO;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceStatisticsDTO2 implements Serializable{
	private LocalDate bookingDate;
    private Double totalRevenue;         
    private Double refundedAmount;      
    private Double netRevenue;   
}
