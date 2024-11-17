package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.StatusRoomDto;
import com.hotel.hotel_stars.Entity.StatusRoom;
import com.hotel.hotel_stars.Models.statusRoomModel;
import com.hotel.hotel_stars.Repository.StatusRoomRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StatusRoomService {
    @Autowired
    private StatusRoomRepository strrep;
    public StatusRoomDto convertToDto(StatusRoom str) {
        return new StatusRoomDto(
                str.getId(),
                str.getStatusRoomName()
        );
    }

    public List<StatusRoomDto> getAllStatusRooms() {
        List<StatusRoom> atrs = strrep.findAll();
        return atrs.stream()
                .map(this::convertToDto)
                .toList();
    }

    public StatusRoomDto addStatusRoom(statusRoomModel strmodel) {
        List<String> errorMessages = new ArrayList<>(); // Danh sách lưu trữ các thông báo lỗi

        // Kiểm tra tên
        if (strmodel.getStatusRoomName() == null || strmodel.getStatusRoomName().isEmpty()) {
            errorMessages.add("Tên trạng thái không được để trống");
        } else if (strrep.existsByStatusRoomName(strmodel.getStatusRoomName())) {
            errorMessages.add("Tên này đã tồn tại");
        }

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(String.join(", ", errorMessages));
        }

        try {
            StatusRoom str = new StatusRoom();
            // In ra màn hình
            System.out.println("ID: " + str.getId());
            System.out.println("Tên trạng thái: " + strmodel.getStatusRoomName());

            str.setStatusRoomName(strmodel.getStatusRoomName());

            // Lưu tài khoản vào cơ sở dữ liệu và chuyển đổi sang DTO
            StatusRoom savedStr = strrep.save(str);
            return convertToDto(savedStr);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Có lỗi xảy ra do vi phạm tính toàn vẹn dữ liệu", e);
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra khi thêm dịch vụ phòng!", e);
        }
    }

    public StatusRoomDto updateStatusRoom(Integer strId, statusRoomModel strmodel) {
        List<String> errorMessages = new ArrayList<>(); // Danh sách lưu trữ các thông báo lỗi

        // Kiểm tra xem tài khoản có tồn tại hay không
        Optional<StatusRoom> existingStrOpt = strrep.findById(strId);

        StatusRoom existingStr = existingStrOpt.get();

        // kiểm tra tên dịch vụ phòng
        if (strmodel.getStatusRoomName() == null || strmodel.getStatusRoomName().isEmpty()) {
            errorMessages.add("Tên dịch vụ phòng không được để trống");
        } else if (!existingStr.getStatusRoomName().equals(strmodel.getStatusRoomName()) && strrep.existsByStatusRoomName(strmodel.getStatusRoomName())) {
            errorMessages.add("Dịch vụ của phòng này đã tồn tại");
        }


        // Nếu có lỗi, ném ngoại lệ với thông báo lỗi
        if (!errorMessages.isEmpty()) {
            throw new ValidationException(String.join(", ", errorMessages));
        }

        try {
            //Cập nhật các thuộc tính cho tài khoản
            existingStr.setStatusRoomName(strmodel.getStatusRoomName());
            StatusRoom updatedStr = strrep.save(existingStr);
            return convertToDto(updatedStr); // Chuyển đổi tài khoản đã lưu sang DTO

        } catch (DataIntegrityViolationException e) {
            // Xử lý lỗi vi phạm tính toàn vẹn dữ liệu (VD: trùng lặp dịch vụ phòng)
            throw new RuntimeException("Có lỗi xảy ra do vi phạm tính toàn vẹn dữ liệu", e);
        } catch (Exception e) {
            // Xử lý lỗi chung
            throw new RuntimeException("Có lỗi xảy ra khi cập nhật dịch vụ phòng", e);
        }
    }

    public void deleteStatusRoom(Integer id) {
        if (!strrep.existsById(id)) {
            throw new NoSuchElementException("Dịch vụ phòng này không tồn tại"); // Ném ngoại lệ nếu không tồn tại
        }
        strrep.deleteById(id);
    }
    
    public List<StatusRoomDto> getByExcludingId(Integer id){
    	List<StatusRoom> statusRooms = strrep.findAllExcludingId(id);
    	return statusRooms.stream().map(this::convertToDto).toList();
    }
}
