package com.hotel.hotel_stars.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.hotel_stars.DTO.BookingRoomServiceRoomDto;
import com.hotel.hotel_stars.Entity.BookingRoom;
import com.hotel.hotel_stars.Entity.BookingRoomServiceRoom;
import com.hotel.hotel_stars.Repository.BookingRoomServiceRoomRepository;

@Service
public class BookingRoomServiceRoomService {

	@Autowired
	BookingRoomService bookingRoomService;
	@Autowired
	ServiceRoomService serviceRoomService;
	@Autowired
	BookingRoomServiceRoomRepository bookingRoomServiceRoomRepository;
	public BookingRoomServiceRoomDto convertDto(BookingRoomServiceRoom bookingRoomServiceRoom) {
		BookingRoomServiceRoomDto dto = new BookingRoomServiceRoomDto();
		dto.setCreateAt(bookingRoomServiceRoom.getCreateAt());
		dto.setId(bookingRoomServiceRoom.getId());
		dto.setPrice(bookingRoomServiceRoom.getPrice());
		dto.setQuantity(bookingRoomServiceRoom.getQuantity());
		dto.setBookingRoomDto(bookingRoomService.toDTO(bookingRoomServiceRoom.getBookingRoom()));
		dto.setServiceRoomDto(serviceRoomService.convertToDto(bookingRoomServiceRoom.getServiceRoom()));
		return dto;
	}
	
	public List<BookingRoomServiceRoomDto> listBookingRoomService(List<Integer> id){
		List<BookingRoomServiceRoom> list = bookingRoomServiceRoomRepository.findByBookingRoomIdIn(id);
		return list.stream().map(this::convertDto).toList();
	}
	
	public List<BookingRoomServiceRoomDto> getBookingRoomByIdService(Integer id){
		List<BookingRoomServiceRoom> service = bookingRoomServiceRoomRepository.findByBookingRoomId(id);
		return service.stream().map(this::convertDto).toList();
	}
}
