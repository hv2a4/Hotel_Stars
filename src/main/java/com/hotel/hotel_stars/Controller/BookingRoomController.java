package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.Entity.BookingRoom;
import com.hotel.hotel_stars.Service.BookingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin("*")
@RequestMapping("api/booking-room")
public class BookingRoomController {
    @Autowired
    private BookingRoomService bookingRoomService;

    @GetMapping("getAll")
    public ResponseEntity<?> getAllBookingRoom() {
        return ResponseEntity.ok(bookingRoomService.getAllBookingRooms());
    }
}
