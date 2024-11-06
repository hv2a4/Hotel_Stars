package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.Models.HotelModel;
import com.hotel.hotel_stars.Service.HotelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/hotel")
public class HotelController {
    @Autowired
    HotelService hotelService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllAccounts() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/getHotel")
    public ResponseEntity<?> getHotel () {
        return ResponseEntity.ok(hotelService.getHotel());
    }
    @PutMapping("/update-hotel")
    public ResponseEntity<StatusResponseDto> updateHotel(
            @Valid @RequestBody HotelModel hotelModel
    ) {
        StatusResponseDto response = hotelService.updateHotel(hotelModel);

        switch (response.getCode()) {
            case "404":
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            case "500":
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            default:
                return ResponseEntity.ok(response);
        }
    }
}
