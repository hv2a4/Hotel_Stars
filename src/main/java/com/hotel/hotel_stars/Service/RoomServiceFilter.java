package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.StatusRoomDto;
import com.hotel.hotel_stars.DTO.TypeRoomDto;
import com.hotel.hotel_stars.Entity.StatusRoom;
import com.hotel.hotel_stars.Entity.TypeRoom;
import com.hotel.hotel_stars.Repository.StatusRoomRepository;
import com.hotel.hotel_stars.Repository.TypeRoomRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceFilter {
    @Autowired
    private StatusRoomRepository statusRoomRepository;

    @Autowired
    TypeRoomRepository typeRoomRepository;

    @Autowired
    ModelMapper modelMapper;

    public StatusRoomDto convert(StatusRoom statusRoom) {
        StatusRoomDto statusRoomDto = modelMapper.map(statusRoom, StatusRoomDto.class);
        return statusRoomDto;
    }

    public List<StatusRoomDto> getAllStatusRoom() {
        List<StatusRoom> statusRoomList = statusRoomRepository.findAll();
        return statusRoomList.stream().map(this::convert).toList();
    }

    public List<TypeRoomDto> searchTypeRoom(String keyword) {
        List<TypeRoom> typeRooms = typeRoomRepository.findByTypeRoomNameContaining(keyword);
        if (typeRooms.isEmpty()) {
            throw new EntityNotFoundException("No type rooms found for the keyword: " + keyword);
        }
        return typeRooms.stream()
                .map(typeRoom -> modelMapper.map(typeRoom, TypeRoomDto.class))
                .toList();
    }
}
