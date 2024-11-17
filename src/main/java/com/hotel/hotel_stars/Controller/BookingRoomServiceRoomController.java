package com.hotel.hotel_stars.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.hotel_stars.Service.BookingRoomServiceRoomService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/booking-room-service-room/")
public class BookingRoomServiceRoomController {

	@Autowired
	BookingRoomServiceRoomService bookingRoomServiceRoomService;
	
	@GetMapping("service")
	public ResponseEntity<?> getServiceRoom(@RequestParam("bookingRoom") List<Integer> id){
		return ResponseEntity.ok(bookingRoomServiceRoomService.listBookingRoomService(id));
	}
}
