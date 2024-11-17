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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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
            // Kiểm tra trùng tên phòng
            if (roomRepository.existsByRoomName(roomModel.getRoomName())) {
                return new StatusResponseDto("409", "Conflict", "Tên phòng đã tồn tại");
            }

            // Tạo đối tượng Room từ RoomModel
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

            // Lưu phòng vào cơ sở dữ liệu
            roomRepository.save(room);

            return new StatusResponseDto("200", "Success", "Phòng đã được thêm thành công");
        } catch (RuntimeException e) {
            return new StatusResponseDto("400", "Bad Request", e.getMessage());
        } catch (Exception e) {
            return new StatusResponseDto("500", "Error", "Có lỗi xảy ra khi thêm phòng: " + e.getMessage());
        }
    }


    public countDto displayCounts() {
        List<Object[]> results = roomRepository.getCounts();
        countDto dto=new countDto();
        for (Object[] result : results) {
            // Chuyển đổi đúng kiểu
            dto.setCountStaff( ((Number) result[0]).longValue());
            dto.setCountCustomers(((Number) result[1]).longValue());
            dto.setTotalRoom(((Number) result[2]).longValue());

        }
        return dto;
    }

    public StatusResponseDto PutRoom(RoomModel roomModel) {
        try {
            Room room = roomRepository.findById(roomModel.getId())
                    .orElseThrow(() -> new RuntimeException("Phòng không tồn tại")); // Handle room not found

            // Validate room data
            // Validate: Check if the room name exists in other rooms
            if (roomRepository.existsByRoomNameAndIdNot(roomModel.getRoomName(), roomModel.getId())) {
                return new StatusResponseDto("409", "Conflict", "Tên phòng đã tồn tại ở một phòng khác");
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

            // Thử xóa phòng
            roomRepository.deleteById(id);
            statusResponseDto.setCode("200");
            statusResponseDto.setStatus("Success");
            statusResponseDto.setMessage("Xóa thành công phòng với ID: " + id);

        } catch (DataIntegrityViolationException e) {
            // Xử lý lỗi khi phòng đang được tham chiếu bởi khóa ngoại (ví dụ: phòng đang được đặt)
            statusResponseDto.setCode("409");
            statusResponseDto.setStatus("Conflict");
            statusResponseDto.setMessage("Không thể xóa phòng này vì đang được sử dụng!");
        } catch (Exception e) {
            // Xử lý các lỗi khác
            statusResponseDto.setCode("500");
            statusResponseDto.setStatus("Error");
            statusResponseDto.setMessage("Xóa thất bại: " + e.getMessage());
        }
        return statusResponseDto;
    }
}
