package com.hotel.hotel_stars.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.hotel_stars.DTO.BookingDto;
import com.hotel.hotel_stars.DTO.InvoiceDto;
import com.hotel.hotel_stars.Entity.Invoice;
import com.hotel.hotel_stars.Repository.InvoiceRepository;

@Service
public class InvoiceService {

	@Autowired
	InvoiceRepository invoiceRepository;
	
	public InvoiceDto convertDto(Invoice invoice) {
		InvoiceDto dto = new InvoiceDto();
		dto.setCreateAt(invoice.getCreateAt());
		dto.setId(invoice.getId());
		dto.setInvoiceStatus(invoice.getInvoiceStatus());
		dto.setTotalAmount(invoice.getTotalAmount());
		return dto;
	}
	public List<InvoiceDto> convertListDtos(List<Invoice> invoices){
		return invoices.stream().map(this::convertDto).toList();
	}
	public InvoiceDto getInvoiceByBooking(Integer id) {
		Invoice invoice = invoiceRepository.findByBooking_Id(id).orElse(null);
		return convertDto(invoice);
	}
}
