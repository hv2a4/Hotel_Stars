package com.hotel.hotel_stars.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.hotel.hotel_stars.DTO.ServiceRoomDto;
import com.hotel.hotel_stars.DTO.TypeServiceRoomDto;
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

	public TypeServiceRoomDto convertToDto(TypeServiceRoom typeServiceRoom) {
	    TypeServiceRoomDto dto = new TypeServiceRoomDto();
	    dto.setId(typeServiceRoom.getId());
	    dto.setServiceRoomName(typeServiceRoom.getServiceRoomName());

	    // Chuyển đổi danh sách ServiceRoom -> ServiceRoomDto
	    if (typeServiceRoom.getServiceRooms() != null) {
	        dto.setServiceRoomDtos(
	            typeServiceRoom.getServiceRooms().stream().map(serviceRoom -> {
	                ServiceRoomDto serviceRoomDto = new ServiceRoomDto();
	                serviceRoomDto.setId(serviceRoom.getId());
	                serviceRoomDto.setServiceRoomName(serviceRoom.getServiceRoomName());
	                serviceRoomDto.setPrice(serviceRoom.getPrice());
	                serviceRoomDto.setImageName(serviceRoom.getImageName());
	                // Không ánh xạ vòng lặp ngược để tránh lỗi stackoverflow
	                serviceRoomDto.setTypeServiceRoomDto(null);
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
