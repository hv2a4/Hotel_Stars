package com.hotel.hotel_stars.Controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.Models.InvoiceModel;
import com.hotel.hotel_stars.Service.InvoiceService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/invoice")
public class InvoiceController {

	@Autowired
	InvoiceService invoiceService;
	
	@GetMapping("/booking/{id}")
	public ResponseEntity<?> getBookingId(@PathVariable("id") Integer id){
		return ResponseEntity.ok(invoiceService.getInvoiceByBooking(id));
	}
	
	@PostMapping("/add")
	public StatusResponseDto addInvoice(@RequestBody InvoiceModel invoiceModel) {
		return invoiceService.addInvoice(invoiceModel);
	}
	@PostMapping("/maintenance")
	public StatusResponseDto hoanthanhbaotri(@RequestBody InvoiceModel invoiceModel) {
		return invoiceService.hoanthanhbaotri(invoiceModel);
	}
	
	//khoi
	
	@GetMapping("/getAll-statistics")
	public ResponseEntity<?> getInvoiceStatisticsAll() {
        return ResponseEntity.ok(invoiceService.getInvoiceStatisticsAll());
    }
	@GetMapping("/getAll-reservation")
	public ResponseEntity<?> getStatisticsAll() {
		return ResponseEntity.ok(invoiceService.getStatisticsAll());
	}
	@GetMapping("/statistics")
    public ResponseEntity<?> getStatistics(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        return ResponseEntity.ok(invoiceService.getInvoiceStatistics(startDate, endDate));
    }
	@GetMapping("/statistics2")
    public ResponseEntity<?> getStatistics2(@RequestParam("startDate") LocalDate startDate,
    		 @RequestParam("endDate") LocalDate endDate) {
        return ResponseEntity.ok(invoiceService.getInvoiceStatisticsByDateRange(startDate, endDate));
    }
	
	
	//khoi
}
