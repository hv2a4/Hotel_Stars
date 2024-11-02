package com.hotel.hotel_stars.Models;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class accountModel {
    private Integer id;

    @NotBlank(message = "Tên người dùng không được để trống")
    @Size(min = 3, max = 20, message = "Tên người dùng phải từ 3 đến 20 ký tự")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String passwords;

    @NotBlank(message = "Họ và tên không được để trống")
    private String fullname;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    private String avatar;


    private Boolean gender;

    private Integer idRoles;
}
