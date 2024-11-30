package com.hotel.hotel_stars.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.hotel_stars.DTO.BookingRoomServiceRoomDto;
import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.Entity.Booking;
import com.hotel.hotel_stars.Entity.BookingRoom;
import com.hotel.hotel_stars.Entity.BookingRoomServiceRoom;
import com.hotel.hotel_stars.Entity.ServiceRoom;
import com.hotel.hotel_stars.Models.BookingRoomServiceRoomModel;
import com.hotel.hotel_stars.Repository.BookingRoomRepository;
import com.hotel.hotel_stars.Repository.BookingRoomServiceRoomRepository;
import com.hotel.hotel_stars.Repository.ServiceRoomRepository;

@Service
public class BookingRoomServiceRoomService {

	@Autowired
	BookingRoomService bookingRoomService;
	@Autowired
	ServiceRoomService serviceRoomService;
	@Autowired
	BookingRoomRepository bookingRoomRepository;
	@Autowired
	ServiceRoomRepository serviceRoomRepository;
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

	public StatusResponseDto add(List<BookingRoomServiceRoomModel> model) {
		try {
			for (BookingRoomServiceRoomModel bookingRoomServiceRoomModel : model) {
				BookingRoomServiceRoom serviceRoomBK = new BookingRoomServiceRoom();
				BookingRoom bookingRoom = bookingRoomRepository.findById(bookingRoomServiceRoomModel.getBookingRoomId())
						.orElseThrow(() -> new RuntimeException("Booking Room không tồn tại"));
				ServiceRoom serviceRoom = serviceRoomRepository.findById(bookingRoomServiceRoomModel.getServiceRoomId())
						.orElseThrow(() -> new RuntimeException("Service Room không tồn tại"));
				serviceRoomBK.setBookingRoom(bookingRoom);
				serviceRoomBK.setCreateAt(bookingRoomServiceRoomModel.getCreateAt());
				serviceRoomBK.setPrice(bookingRoomServiceRoomModel.getPrice());
				serviceRoomBK.setQuantity(bookingRoomServiceRoomModel.getQuantity());
				serviceRoomBK.setServiceRoom(serviceRoom);
				bookingRoomServiceRoomRepository.save(serviceRoomBK);
			}

			return new StatusResponseDto("200", "success", "Dịch vụ đã được thêm thành công");
		} catch (RuntimeException e) {
		    return new StatusResponseDto("400", "error", "Lỗi không thể chuyền dữ liệu");
		} catch (Exception e) {
			return new StatusResponseDto("500", "error", "Có lỗi xảy ra khi thêm dịch vụ: " + e.getMessage());
		}

	}
	public BookingRoomServiceRoomDto updateQuantity(Integer id, BookingRoomServiceRoomModel model) {
	    BookingRoomServiceRoom service = bookingRoomServiceRoomRepository.findById(id).orElseThrow(() -> new RuntimeException("Service not found"));
	    Integer quantity = model.getQuantity();
	    if (quantity == null) {
	        throw new IllegalArgumentException("Số lượng không được null");
	    }
	    service.setQuantity(quantity);
	    if (service.getPrice() != null) {
	        service.setPrice(service.getPrice());
	    } else {
	        throw new IllegalArgumentException("Giá không thể null");
	    }
	    BookingRoomServiceRoom saveService = bookingRoomServiceRoomRepository.save(service);
	    return convertDto(saveService);
	}
	
	public StatusResponseDto delete(Integer id) {
	    Optional<BookingRoomServiceRoom> serviceRoom = bookingRoomServiceRoomRepository.findById(id);
	    if (serviceRoom.isPresent()) {
	        bookingRoomServiceRoomRepository.deleteById(id);
	        return new StatusResponseDto("200", "success","Xóa thành công");
	    } else {
	        return new StatusResponseDto("400", "error","Xóa thất bại");
	    }
	}


	public List<BookingRoomServiceRoomDto> listBookingRoomService(List<Integer> id) {
		List<BookingRoomServiceRoom> list = bookingRoomServiceRoomRepository.findByBookingRoomIdIn(id);
		return list.stream().map(this::convertDto).toList();
	}

	public List<BookingRoomServiceRoomDto> getBookingRoomByIdService(Integer id) {
		List<BookingRoomServiceRoom> service = bookingRoomServiceRoomRepository.findByBookingRoomId(id);
		return service.stream().map(this::convertDto).toList();
	}
}
