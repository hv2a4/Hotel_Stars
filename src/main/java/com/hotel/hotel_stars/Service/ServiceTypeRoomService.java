package com.hotel.hotel_stars.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.hotel.hotel_stars.DTO.BookingRoomServiceRoomDto;
import com.hotel.hotel_stars.DTO.ServiceRoomDto;
import com.hotel.hotel_stars.DTO.TypeServiceRoomDto;
import com.hotel.hotel_stars.Entity.BookingRoomServiceRoom;
import com.hotel.hotel_stars.Entity.ServiceRoom;
import com.hotel.hotel_stars.Entity.TypeServiceRoom;
import com.hotel.hotel_stars.Models.typeRoomServiceModel;
import com.hotel.hotel_stars.Repository.ServiceRoomRepository;
import com.hotel.hotel_stars.Repository.TypeRoomServiceRepository;

@Service
public class ServiceTypeRoomService {
	@Autowired
	TypeRoomServiceRepository typeRoomServiceRepository;
	@Autowired
	ServiceRoomRepository serviceRoomRepository;

	@Autowired
	BookingRoomService bookingRoomService;
    //khôi

    public BookingRoomServiceRoomDto convertDto(BookingRoomServiceRoom bookingRoomServiceRoom) {
		BookingRoomServiceRoomDto dto = new BookingRoomServiceRoomDto();
		dto.setCreateAt(bookingRoomServiceRoom.getCreateAt());
		dto.setId(bookingRoomServiceRoom.getId());
		dto.setPrice(bookingRoomServiceRoom.getPrice());
		dto.setQuantity(bookingRoomServiceRoom.getQuantity());
		dto.setBookingRoomDto(bookingRoomService.toDTO(bookingRoomServiceRoom.getBookingRoom()));
		dto.setServiceRoomDto(null);
		return dto;
	}
    
	public TypeServiceRoomDto convertToDto(TypeServiceRoom typeServiceRoom) {
	    TypeServiceRoomDto dto = new TypeServiceRoomDto();
	    dto.setId(typeServiceRoom.getId());
	    dto.setServiceRoomName(typeServiceRoom.getServiceRoomName());
	    dto.setDuration(typeServiceRoom.getDuration());

	    // Chuyển đổi danh sách ServiceRoom -> ServiceRoomDto
	    if (typeServiceRoom.getServiceRooms() != null) {
	        dto.setServiceRoomDtos(
	            typeServiceRoom.getServiceRooms().stream().map(serviceRoom -> {
	            	 List<BookingRoomServiceRoomDto> bookingRoomServiceRoomDtos = serviceRoom.getBookingRoomServiceRooms().stream().map(this::convertDto).toList();
	                ServiceRoomDto serviceRoomDto = new ServiceRoomDto();
	                serviceRoomDto.setId(serviceRoom.getId());
	                serviceRoomDto.setServiceRoomName(serviceRoom.getServiceRoomName());
	                serviceRoomDto.setPrice(serviceRoom.getPrice());
	                serviceRoomDto.setImageName(serviceRoom.getImageName());
	                serviceRoomDto.setBookingRoomServiceRooms(bookingRoomServiceRoomDtos);
	                TypeServiceRoomDto typeServiceRoomDto = new TypeServiceRoomDto();
	                typeServiceRoomDto.setId(serviceRoom.getTypeServiceRoomId().getId());
	                typeServiceRoomDto.setDuration(serviceRoom.getTypeServiceRoomId().getDuration());
	                typeServiceRoomDto.setServiceRoomName(serviceRoom.getTypeServiceRoomId().getServiceRoomName());
	                serviceRoomDto.setTypeServiceRoomDto(typeServiceRoomDto);
	                return serviceRoomDto;
	            }).collect(Collectors.toSet())
	        );
	    } else {
	        dto.setServiceRoomDtos(null);
	    }
	    return dto;
	}

	public List<TypeServiceRoomDto> convertToDtoList(List<TypeServiceRoom> typeServiceRooms) {
		return typeServiceRooms.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	public List<TypeServiceRoomDto> getAll() {
		List<TypeServiceRoom> typeServiceRooms = typeRoomServiceRepository.findAll();
		return convertToDtoList(typeServiceRooms);
	}

	public TypeServiceRoomDto createTypeServiceRoom(typeRoomServiceModel model) {
		if (model.getServiceRoomName() == null) {
			throw new RuntimeException("Vui lòng nhập đầy đủ thông tin");
		}
		TypeServiceRoom typeServiceRoom = new TypeServiceRoom();
		typeServiceRoom.setServiceRoomName(model.getServiceRoomName());
		typeRoomServiceRepository.save(typeServiceRoom);
		return convertToDto(typeServiceRoom);
	}

	public TypeServiceRoomDto updateTypeServiceRoom(Integer id, typeRoomServiceModel model) {
		if (model.getServiceRoomName() == null) {
			throw new RuntimeException("Vui lòng nhập đầy đủ thông tin");
		}
		TypeServiceRoom typeServiceRoom = typeRoomServiceRepository.findById(id).get();
		typeServiceRoom.setServiceRoomName(model.getServiceRoomName());
		typeRoomServiceRepository.save(typeServiceRoom);

		return convertToDto(typeServiceRoom);
	}

	public String deleteTypeRoomService(Integer id) {
		try {
			typeRoomServiceRepository.deleteById(id);
			return "Xóa thành công!";
		} catch (DataIntegrityViolationException e) {
			return "Không thể xóa vì đang có dữ liệu liên quan!";
		} catch (Exception e) {
			return "Có lỗi xảy ra trong quá trình xóa!";
		}
	}

}
