package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.DTO.DiscountAccountDto;
import com.hotel.hotel_stars.Service.DiscountAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/discount-accounts")
public class DiscountAccountController {
    @Autowired
    private DiscountAccountService discountAccountService;

    @GetMapping("getAll")
    public ResponseEntity<?> getAllDiscountAccounts() {
        return ResponseEntity.ok(discountAccountService.getDiscountAccountDtoList());
    }
}
