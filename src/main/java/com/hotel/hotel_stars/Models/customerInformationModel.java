package com.hotel.hotel_stars.Models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Data
@Getter
@Setter
public class customerInformationModel {
    private Integer id;

    @NotBlank(message = "Không được để trống CCCD")
    private String cccd;

    @NotBlank(message = "Không được để trống họ tên")
    private String fullname;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\d{10}$", message = "Số điện thoại phải có 10 chữ số")  // Kiểm tra số điện thoại có đúng định dạng
    @Pattern(regexp = "^(?:\\+84|0)\\d{9}$", message = "Số điện thoại không hợp lệ, phải bắt đầu với 0 hoặc +84 và có 10 chữ số")
    private String phone;

    @NotNull(message = "Vui lòng chọn giới tính")
    private Boolean gender;

    @NotNull(message = "Ngày sinh không được để trống")
    @Past(message = "Ngày sinh không thể là ngày trong tương lai")  // Kiểm tra ngày sinh hợp lệ
    private Instant birthday;

    private String imgFirstCard;

    private String imgLastCard;


}
