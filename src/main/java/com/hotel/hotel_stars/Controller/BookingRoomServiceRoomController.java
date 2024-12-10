package com.hotel.hotel_stars.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.Entity.BookingRoomServiceRoom;
import com.hotel.hotel_stars.Models.BookingRoomServiceRoomModel;
import com.hotel.hotel_stars.Service.BookingRoomServiceRoomService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/booking-room-service-room")
public class BookingRoomServiceRoomController {

	@Autowired
	BookingRoomServiceRoomService bookingRoomServiceRoomService;
	
	@GetMapping("/service")
	public ResponseEntity<?> getServiceRoom(@RequestParam("bookingRoom") List<Integer> id){
		return ResponseEntity.ok(bookingRoomServiceRoomService.listBookingRoomService(id));
	}
	//khôi
	@GetMapping("/booking-room-id")
	public ResponseEntity<?> getBookingRoomIdService(@RequestParam("bookingRoom") Integer id){
		return ResponseEntity.ok(bookingRoomServiceRoomService.getBookingRoomByIdService(id));
	}
	@PostMapping
	public ResponseEntity<?> add(@RequestBody List<BookingRoomServiceRoomModel> model){
		return ResponseEntity.ok(bookingRoomServiceRoomService.add(model));
	}
	@PostMapping("/post-service")
	public ResponseEntity<?> addNew(@RequestBody List<BookingRoomServiceRoomModel> model, @RequestParam("bookingRoomId") List<Integer> bookingRoomId){
		return ResponseEntity.ok(bookingRoomServiceRoomService.addNew(model, bookingRoomId));
	}
	@PutMapping("/{id}")
	public ResponseEntity<?> updateQuantity(@PathVariable("id") Integer id,@RequestBody BookingRoomServiceRoomModel model){
		return ResponseEntity.ok(bookingRoomServiceRoomService.updateQuantity(id, model));
	}
	@DeleteMapping("/{id}")
	public StatusResponseDto delete(@PathVariable("id") Integer id){
		return bookingRoomServiceRoomService.delete(id);
	}
	//khôi
}
