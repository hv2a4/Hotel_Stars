package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/list-controller")
public class ListStaffController {

    @Autowired
    AccountService accountService;

    @GetMapping("get-staff")
    public ResponseEntity<?> listStaff() {
        return ResponseEntity.ok().body(accountService.getAccountRoles());
    }
}
