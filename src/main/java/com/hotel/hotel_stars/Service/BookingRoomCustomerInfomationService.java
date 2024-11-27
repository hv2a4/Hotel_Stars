package com.hotel.hotel_stars.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.hotel_stars.DTO.BookingRoomCustomerInformationDto;
import com.hotel.hotel_stars.Entity.BookingRoomCustomerInformation;
import com.hotel.hotel_stars.Repository.BookingRoomCustomerInformationRepository;

@Service
public class BookingRoomCustomerInfomationService {
	@Autowired
	BookingRoomService bookingRoomService;
	@Autowired
	CustomerInformationService customerInformationService;
	@Autowired
	BookingRoomCustomerInformationRepository bookingRoomCustomerInformationRepository;
	public BookingRoomCustomerInformationDto convertDto(BookingRoomCustomerInformation customer) {
		BookingRoomCustomerInformationDto dto = new BookingRoomCustomerInformationDto();
		dto.setId(customer.getId());
		dto.setBookingRoomDto(bookingRoomService.toDTO(customer.getBookingRoom()));
		dto.setCustomerInformationDto(customerInformationService.convertToDto(customer.getCustomerInformation()));
		return dto;
	}
	
//	public List<BookingRoomCustomerInformationDto> getListBookingRoom_Id(Integer id){
//		List<BookingRoomCustomerInformation> list = bookingRoomCustomerInformationRepository.findByBookingRoom_Id(id);
//		return list.stream().map(this::convertDto).toList();
//	}
	public List<BookingRoomCustomerInformationDto> getListBookingRoom_Id(List<Integer> bookingRoomIds) {
	    List<BookingRoomCustomerInformation> list = bookingRoomCustomerInformationRepository.findByBookingRoom_IdIn(bookingRoomIds);
	    return list.stream().map(this::convertDto).collect(Collectors.toList());
	}
	
	public boolean deleteCustomer(Integer idCustomer, Integer idBookingRoom) {
	    try {
	    	System.out.println("mã khách hàng: "+idCustomer+" mã booking: "+idBookingRoom);
	        BookingRoomCustomerInformation customer = bookingRoomCustomerInformationRepository
	                .findByBookingRoomIdAndCustomerInformationId(idBookingRoom, idCustomer);
	        
	        if (customer == null) {
	            return false;
	        }
	        bookingRoomCustomerInformationRepository.deleteById(customer.getId());
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}

}
