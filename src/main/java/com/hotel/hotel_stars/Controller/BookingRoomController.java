package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.Entity.BookingRoom;
import com.hotel.hotel_stars.Service.BookingRoomService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/account/{id}")
    public ResponseEntity<?> getBookingRoomAccount(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(bookingRoomService.getBookingRoomAccount(id));
    }

    @GetMapping("/room")
    public ResponseEntity<?> getBookingRoomByRoom(@RequestParam("roomId") Integer roomId, @RequestParam("statusId") Integer statusId) {
        return ResponseEntity.ok(bookingRoomService.getByRoom(roomId, statusId));
    }

    @GetMapping("/list-booking-room")
    public ResponseEntity<?> getBookingRoomInIds(@RequestParam("bookingRoomId") List<Integer> roomId) {
        return ResponseEntity.ok(bookingRoomService.getBookingRoomIds(roomId));
    }

    //khoi
    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(bookingRoomService.getByIdBookingRoom(id));
    }

    @GetMapping("/getByRoom/{idRoom}")
    public ResponseEntity<?> getByRoom(@PathVariable("idRoom") Integer idRoom) {
        return ResponseEntity.ok(bookingRoomService.getBookingRoomByRoom(idRoom));
    }
    //khoi
}
