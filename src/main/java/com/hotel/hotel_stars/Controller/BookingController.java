package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.Config.JwtService;
import com.hotel.hotel_stars.DTO.Select.BookingDetailDTO;
import com.hotel.hotel_stars.DTO.Select.PaymentInfoDTO;
import com.hotel.hotel_stars.Entity.Booking;
import com.hotel.hotel_stars.Entity.BookingRoom;
import com.hotel.hotel_stars.Entity.StatusBooking;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Exception.ErrorsService;
import com.hotel.hotel_stars.Models.bookingModel;
import com.hotel.hotel_stars.Repository.BookingRepository;
import com.hotel.hotel_stars.Repository.BookingRoomRepository;
import com.hotel.hotel_stars.Repository.StatusBookingRepository;
import com.hotel.hotel_stars.Service.BookingService;
import com.hotel.hotel_stars.utils.paramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/booking/")
public class BookingController {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private ErrorsService errorsServices;
    @Autowired
    private BookingRoomRepository bookingRoomRepository;
    @Autowired
    private StatusBookingRepository statusBookingRepository;
    @Autowired
    private paramService paramServices;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private BookingRepository bookingRepository;
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<BookingDetailDTO>> getBookingDetails(@PathVariable Integer accountId) {
        return ResponseEntity.ok(bookingService.getBookingDetailsByAccountId(accountId)) ;
    }

    @GetMapping("/account/payment-info/{id}")
    public ResponseEntity<List<PaymentInfoDTO>> getBookingPaymentInfo(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.getPaymentInfoByAccountId(id));
    }
    
    @GetMapping("/accountId/{id}")
    public ResponseEntity<?> getBookingByAccount(@PathVariable("id") Integer id){
    	return ResponseEntity.ok(bookingService.getListByAccountId(id));
    }
}
