package com.hotel.hotel_stars.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.hotel_stars.Service.InvoiceService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/invoice/")
public class InvoiceController {

	@Autowired
	InvoiceService invoiceService;
	
	@GetMapping("booking/{id}")
	public ResponseEntity<?> getBookingId(@PathVariable("id") Integer id){
		return ResponseEntity.ok(invoiceService.getInvoiceByBooking(id));
	}
}
