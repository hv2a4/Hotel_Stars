package com.hotel.hotel_stars.Models;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class accountModel {
    private Integer id;


    private String username;
    private String passwords;

    @NotBlank(message = "Không được bỏ trống họ tên")
    @Size(min = 6, message = "Họ tên phải có ít nhất 6 ký tự")
    private String fullname;

    @NotBlank(message = "Không được bỏ trống số điện thoại")
    private String phone;

    @NotBlank(message = "Không được bỏ trống số email")
    private String email;

    private String avatar;

    private Boolean gender;

    private Integer idRoles;
}
