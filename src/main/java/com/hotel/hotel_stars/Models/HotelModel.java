package com.hotel.hotel_stars.Models;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelModel {

    private Integer id;

    @NotBlank(message = "Tên khách sạn là bắt buộc.")
    @Size(max = 100, message = "Tên khách sạn không được vượt quá 100 ký tự.")
    private String hotelName;

    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự.")
    private String descriptions;

    @NotBlank(message = "Tỉnh/Thành phố là bắt buộc.")
    private String province;

    @NotBlank(message = "Quận/Huyện là bắt buộc.")
    private String district;

    @NotBlank(message = "Phường/Xã là bắt buộc.")
    private String ward;

    @NotBlank(message = "Địa chỉ là bắt buộc.")
    private String address;
}
