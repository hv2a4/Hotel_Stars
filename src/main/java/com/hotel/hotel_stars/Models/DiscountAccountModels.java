package com.hotel.hotel_stars.Models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountAccountModels {
    Integer id;

    @NotNull(message = "Id discount không được để trống.")
    Integer discount_id;

    @NotNull(message = "Id account không được để trống.")
    Integer account_id;
}
