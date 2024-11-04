package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.*;
import com.hotel.hotel_stars.DTO.selectDTO.countDto;
import com.hotel.hotel_stars.Entity.Floor;
import com.hotel.hotel_stars.Entity.Room;
import com.hotel.hotel_stars.Entity.StatusRoom;
import com.hotel.hotel_stars.Entity.TypeRoom;
import com.hotel.hotel_stars.Models.RoomModel;
import com.hotel.hotel_stars.Repository.RoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    ModelMapper modelMapper;

    public RoomDto convertToDto(Room room) {
        TypeRoomDto typeRoomDto = modelMapper.map(room, TypeRoomDto.class);
        StatusRoomDto statusRoomDto = modelMapper.map(room, StatusRoomDto.class);
        FloorDto floorDto = modelMapper.map(room, FloorDto.class);
        RoomDto roomDto = modelMapper.map(room, RoomDto.class);
        roomDto.setFloorDto(floorDto);
        roomDto.setTypeRoomDto(typeRoomDto);
        roomDto.setStatusRoomDto(statusRoomDto);
        return modelMapper.map(room, RoomDto.class);
    }

    public List<RoomDto> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream().map(this::convertToDto).toList();
    }

    public RoomDto getById(Integer id) {
        Room room = roomRepository.findById(id).get();
        return convertToDto(room);
    }

    public StatusResponseDto PostRoom(RoomModel roomModel) {
        try {
            // Validate room data
            if (roomModel.getRoomName() == null || roomModel.getRoomName().isEmpty()) {
                throw new RuntimeException("Tên phòng không được để trống");
            }
            // Add more validations as needed

            Room room = new Room();
            TypeRoom roomType = new TypeRoom();
            roomType.setId(roomModel.getTypeRoomId());
            StatusRoom statusRoom = new StatusRoom();
            statusRoom.setId(roomModel.getStatusRoomId());
            Floor floor = new Floor();
            floor.setId(roomModel.getFloorId());
            room.setRoomName(roomModel.getRoomName());
            room.setTypeRoom(roomType);
            room.setStatusRoom(statusRoom);
            room.setFloor(floor);

            roomRepository.save(room); // Save the room

            return new StatusResponseDto("200", "Success", "Phòng đã được thêm thành công");
        } catch (RuntimeException e) {
            return new StatusResponseDto("400", "Bad Request", e.getMessage());
        } catch (Exception e) {
            // Log the exception if necessary
            return new StatusResponseDto("500", "Error", "Có lỗi xảy ra khi thêm phòng: " + e.getMessage());
        }
    }

    public List<countDto> displayCounts() {
        List<Object[]> results = roomRepository.getCounts();
        List<countDto> listDto = new ArrayList<>();
        for (Object[] result : results) {
            Long countStaff = ((Number) result[0]).longValue(); // Chuyển đổi đúng kiểu
            Long countCustomers = ((Number) result[1]).longValue(); // Chuyển đổi đúng kiểu
            Long totalRooms = ((Number) result[2]).longValue(); // Chuyển đổi đúng kiểu
            countDto dto =new countDto(countStaff,countCustomers,totalRooms);
            listDto.add(dto);
        }
        return listDto;
    }

    public StatusResponseDto PutRoom(RoomModel roomModel) {
        try {
            Room room = roomRepository.findById(roomModel.getId())
                    .orElseThrow(() -> new RuntimeException("Phòng không tồn tại")); // Handle room not found

            // Validate room data
            if (roomModel.getRoomName() == null || roomModel.getRoomName().isEmpty()) {
                throw new RuntimeException("Tên phòng không được để trống");
            }
            // Add more validations as needed

            TypeRoom roomType = new TypeRoom();
            roomType.setId(roomModel.getTypeRoomId());
            StatusRoom statusRoom = new StatusRoom();
            statusRoom.setId(roomModel.getStatusRoomId());
            Floor floor = new Floor();
            floor.setId(roomModel.getFloorId());
            room.setRoomName(roomModel.getRoomName());
            room.setTypeRoom(roomType);
            room.setStatusRoom(statusRoom);
            room.setFloor(floor);

            roomRepository.save(room); // Save the updated room

            return new StatusResponseDto("200", "Success", "Phòng đã được cập nhật thành công");
        } catch (RuntimeException e) {
            return new StatusResponseDto("400", "Bad Request", e.getMessage());
        } catch (Exception e) {
            // Log the exception if necessary
            return new StatusResponseDto("500", "Error", "Có lỗi xảy ra khi cập nhật phòng: " + e.getMessage());
        }
    }

    public StatusResponseDto deleteById(Integer id) {
        StatusResponseDto statusResponseDto = new StatusResponseDto();
        try {
            // Kiểm tra xem phòng có tồn tại trước khi xóa
            if (!roomRepository.existsById(id)) {
                statusResponseDto.setCode("404");
                statusResponseDto.setStatus("Not Found");
                statusResponseDto.setMessage("Không tìm thấy phòng với ID: " + id);
                return statusResponseDto;
            }

            roomRepository.deleteById(id);
            statusResponseDto.setCode("200");
            statusResponseDto.setStatus("Success");
            statusResponseDto.setMessage("Xóa thành công phòng với ID: " + id);
        } catch (Exception e) {
            statusResponseDto.setCode("500");
            statusResponseDto.setStatus("Error");
            statusResponseDto.setMessage("Xóa thất bại: " + e.getMessage());
        }
        return statusResponseDto;
    }

}
