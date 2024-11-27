package com.hotel.hotel_stars.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.hotel_stars.Service.BookingRoomCustomerInfomationService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/booking-infomation")
public class BookingRoomCustomerInformationController {

	@Autowired
	BookingRoomCustomerInfomationService bookingRoomCustomerInfomationService;
	
	@GetMapping("/booking-room")
	public ResponseEntity<?> getBookingRoomId(@RequestParam("bookingroom") List<Integer> id){
		return ResponseEntity.ok(bookingRoomCustomerInfomationService.getListBookingRoom_Id(id));
	}
	
	@DeleteMapping
	public boolean deleteCustomer(@RequestParam("idBookingRoom") Integer idBookingRoom, @RequestParam("idCustomer") Integer idCustomer){
		boolean flag = bookingRoomCustomerInfomationService.deleteCustomer(idCustomer, idBookingRoom);
		if (flag) {
			return true;
		} else {
			return false;
		}
		
	}
}
