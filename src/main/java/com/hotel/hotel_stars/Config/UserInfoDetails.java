package com.hotel.hotel_stars.Config;


import com.hotel.hotel_stars.Entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfoDetails implements UserDetails {

    private String name;
    private String password;
    private String roles="";

    private List<GrantedAuthority> authorities;

    public UserInfoDetails(Account userInfo) {
        name = userInfo.getUsername();
        password = userInfo.getPasswords();
        roles = "";
        if (userInfo.getRole() != null) {
            String roleName = userInfo.getRole().getRoleName();
            if (roleName.equalsIgnoreCase("Customer")) {
                roles = "Customer";
            } else if (roleName.equalsIgnoreCase("Staff")) {
                roles = "Staff";
            } else if (roleName.equalsIgnoreCase("HotelOwner")) {
                roles = "HotelOwner";
            } else if (roleName.equalsIgnoreCase("Admin")) {
                roles = "Admin";
            }
        }
        System.out.println(roles+"2222");
        if (!roles.isEmpty()) {
            authorities = Arrays.stream(roles.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            System.out.println(authorities);
        } else {
            authorities = new ArrayList<>(); // Hoặc ném ngoại lệ nếu không có quyền
            System.out.println(authorities+"2");
        }
        //status=userInfo.getStatus();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }



}
