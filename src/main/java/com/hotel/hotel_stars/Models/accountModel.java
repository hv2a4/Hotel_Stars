package com.hotel.hotel_stars.Models;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class accountModel {
    private Integer id;

    @Size(min = 3, max = 20, message = "Tên người dùng phải từ 3 đến 20 ký tự")
    private String username;

    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String passwords;

    private String fullname;

    private String phone;

    @Email(message = "Email không hợp lệ")
    private String email;

    private String avatar;

    @NotNull(message = "Giới tính không được để trống")
    private Boolean gender;

    private Integer idRoles;
}
