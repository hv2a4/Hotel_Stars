package com.hotel.hotel_stars.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.io.Serializable;

/**
 * DTO for {@link com.hotel.hotel_stars.Entity.DiscountAccount}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountAccountDto implements Serializable {
    Integer id;
    Boolean statusDa;
    AccountDto accountDto;
    DiscountDto discountDto;
}