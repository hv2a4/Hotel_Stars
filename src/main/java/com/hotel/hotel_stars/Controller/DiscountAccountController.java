package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.DTO.DiscountAccountDto;
import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.Models.DiscountAccountModels;
import com.hotel.hotel_stars.Service.DiscountAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add")
    public StatusResponseDto addDiscountAccount(@Valid @RequestBody DiscountAccountModels discountAccountModel) {
        return discountAccountService.add(discountAccountModel);
    }
}
