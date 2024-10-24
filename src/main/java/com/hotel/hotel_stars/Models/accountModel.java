package com.hotel.hotel_stars.Models;

import lombok.Data;

@Data
public class accountModel {

    private Integer id;
    private String username;

    private String passwords;

    private String fullname;

    private String phone;

    private String email;

    public String avatar;
    public Boolean gender;

    Integer idRoles;
}
