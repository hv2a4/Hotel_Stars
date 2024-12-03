package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.*;
import com.hotel.hotel_stars.Entity.Floor;
import com.hotel.hotel_stars.Entity.Hotel;
import com.hotel.hotel_stars.Entity.Room;
import com.hotel.hotel_stars.Models.floorModel;
import com.hotel.hotel_stars.Repository.FloorRepository;
import com.hotel.hotel_stars.Repository.HotelRepository;
import com.hotel.hotel_stars.Repository.RoomRepository;
import jakarta.validation.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FloorService {
    @Autowired
    private FloorRepository floorrep;

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    RoomService roomService;
    @Autowired
    TypeRoomService typeRoomService;
    @Autowired
    StatusRoomService statusRoomService;

    public RoomDto convertToDto(Room room) {
        RoomDto roomDto = new RoomDto();
        roomDto.setFloorDto(convertToDto(room.getFloor()));
        roomDto.setTypeRoomDto(typeRoomService.convertTypeRoomDto(room.getTypeRoom()));
        roomDto.setStatusRoomDto(statusRoomService.convertToDto(room.getStatusRoom()));
        return roomDto;
    }
    public FloorDto convertToDto(Floor fl) {
        FloorDto newFloorDto = new FloorDto();
        newFloorDto.setId(fl.getId());
        newFloorDto.setFloorName(fl.getFloorName());
        newFloorDto.setRoomDtos(convertRoom(fl));
        return newFloorDto;
    }

    public List<RoomDto> convertRoom(Floor fl) {
        List<Room> rooms = roomRepository.findByFloorId(fl.getId());
        return rooms.stream()
                .map(this::convertRoomDto)  // Map rooms without recursive floor mapping
                .toList();
    }

    public RoomDto convertRoomDto(Room room) {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(room.getId());
        roomDto.setRoomName(room.getRoomName());
        roomDto.setTypeRoomDto(typeRoomService.convertTypeRoomDto(room.getTypeRoom()));
        roomDto.setStatusRoomDto(statusRoomService.convertToDto(room.getStatusRoom()));
        //roomDto.setFloorDto(convertToDto(room.getFloor()));
        return roomDto;
    }
//    public List<FloorDto> convertToDto(List<Floor> fl) {
//        List<FloorDto> dtos = new ArrayList<>();
//        for (Floor fld : fl) {
//            dtos.add(convertToDto(fld));
//        }
//        return dtos;
//    }


    public List<FloorDto> getAllFloors() {
        List<Floor> fls = floorrep.findAll();
        return fls.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public FloorDto addFloor(floorModel flmodel) {
        List<String> errorMessages = new ArrayList<>(); // Danh sách lưu trữ các thông báo lỗi

        // Kiểm tra tên
        if (flmodel.getFloorName() == null || flmodel.getFloorName().isEmpty()) {
            errorMessages.add("Tên tầng không được để trống");
        } else if (floorrep.existsByFloorName(flmodel.getFloorName())) {
            errorMessages.add("Tên này đã tồn tại");
        }

        if (!errorMessages.isEmpty()) {
            throw new RuntimeException(String.join(", ", errorMessages));
        }

        try {
            Floor fl = new Floor();

            fl.setFloorName(flmodel.getFloorName());

            // Lưu tài khoản vào cơ sở dữ liệu và chuyển đổi sang DTO
            Floor savedFl = floorrep.save(fl);
            return convertToDto(savedFl);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Có lỗi xảy ra do vi phạm tính toàn vẹn dữ liệu", e);
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra khi thêm!", e);
        }
    }

    public FloorDto updateFloor(floorModel flmodel) {
        List<String> errorMessages = new ArrayList<>(); // Danh sách lưu trữ các thông báo lỗi

        // Kiểm tra xem tài khoản có tồn tại hay không
        Optional<Floor> existingFlOpt = floorrep.findById(flmodel.getId());

        Floor existingFl = existingFlOpt.get();

        // kiểm tra tên dịch vụ phòng
        if (flmodel.getFloorName() == null || flmodel.getFloorName().isEmpty()) {
            errorMessages.add("Tên tầng không được để trống");
        } else if (!existingFl.getFloorName().equals(flmodel.getFloorName()) && floorrep.existsByFloorName(flmodel.getFloorName())) {
            errorMessages.add("Tầng này đã tồn tại");
        }


        // Nếu có lỗi, ném ngoại lệ với thông báo lỗi
        if (!errorMessages.isEmpty()) {
            throw new ValidationException(String.join(", ", errorMessages));
        }

        try {
            //Cập nhật các thuộc tính cho tài khoản
            existingFl.setFloorName(flmodel.getFloorName());
            Floor updatedFl = floorrep.save(existingFl);
            return convertToDto(updatedFl); // Chuyển đổi tài khoản đã lưu sang DTO

        } catch (DataIntegrityViolationException e) {
            // Xử lý lỗi vi phạm tính toàn vẹn dữ liệu (VD: trùng lặp dịch vụ phòng)
            throw new RuntimeException("Có lỗi xảy ra do vi phạm tính toàn vẹn dữ liệu", e);
        } catch (Exception e) {
            // Xử lý lỗi chung
            throw new RuntimeException("Có lỗi xảy ra khi cập nhật dịch vụ phòng", e);
        }
    }

    public void deleteFloor(Integer id) {
        if (!floorrep.existsById(id)) {
            throw new NoSuchElementException("Tầng này không tồn tại"); // Ném ngoại lệ nếu không tồn tại
        }
        floorrep.deleteById(id);
    }   
}
