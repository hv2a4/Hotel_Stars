package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.AmenitiesTypeRoomDto;
import com.hotel.hotel_stars.Entity.AmenitiesTypeRoom;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Models.amenitiesTypeRoomModel;
import com.hotel.hotel_stars.Repository.AmenitiesTypeRoomRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AmenitiesTypeRoomService {
    @Autowired
    private AmenitiesTypeRoomRepository atrrep;
    public AmenitiesTypeRoomDto convertToDto(AmenitiesTypeRoom atr) {
        return new AmenitiesTypeRoomDto(
                atr.getId(),
                atr.getAmenitiesTypeRoomName()
//                atr.getIcon()
        );
    }

    public List<AmenitiesTypeRoomDto> getAllAmenitiesTypeRooms() {
        List<AmenitiesTypeRoom> atrs = atrrep.findAll();
        return atrs.stream()
                .map(this::convertToDto)
                .toList();
    }

    public boolean checkIfExistsByName(String name) {
        return atrrep.existsByAmenitiesTypeRoomName(name);
    }

    public AmenitiesTypeRoomDto getAmenitiesTypeRoomById(Integer id) {
        Optional<AmenitiesTypeRoom> atrOpt = atrrep.findById(id);
        if (atrOpt.isEmpty()) {
            throw new NoSuchElementException("Dịch vụ phòng này không tồn tại"); // Ném ngoại lệ nếu không tìm thấy
        }
        return convertToDto(atrOpt.get()); // Trả về đối tượng DTO sau khi tìm thấy
    }

    public AmenitiesTypeRoomDto addAmenitiesTypeRoom(amenitiesTypeRoomModel atrmodel) {
        List<String> errorMessages = new ArrayList<>(); // Danh sách lưu trữ các thông báo lỗi

        // Kiểm tra tên
        if (atrmodel.getAmenitiesTypeRoomName() == null || atrmodel.getAmenitiesTypeRoomName().isEmpty()) {
            errorMessages.add("Tên tiện nghi loại phòng phòng không được để trống");
        } else if (atrrep.existsByAmenitiesTypeRoomName(atrmodel.getAmenitiesTypeRoomName())) {
            errorMessages.add("Tên tiện nghi loại phòng này đã tồn tại");
        }
        if (!errorMessages.isEmpty()) {
            throw new ValidationException(String.join(", ", errorMessages));
        }

        try {
            AmenitiesTypeRoom atr = new AmenitiesTypeRoom();
            // In ra màn hình
            atr.setAmenitiesTypeRoomName(atrmodel.getAmenitiesTypeRoomName());
            // Lưu tài khoản vào cơ sở dữ liệu và chuyển đổi sang DTO
            AmenitiesTypeRoom savedAtr = atrrep.save(atr);
            return convertToDto(savedAtr);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Có lỗi xảy ra do vi phạm tính toàn vẹn dữ liệu", e);
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra khi thêm!", e);
        }
    }

    public AmenitiesTypeRoomDto updateAmenitiesTypeRoom(amenitiesTypeRoomModel atrmodel) {
        List<String> errorMessages = new ArrayList<>(); // Danh sách lưu trữ các thông báo lỗi

        // Kiểm tra xem tài khoản có tồn tại hay không
        Optional<AmenitiesTypeRoom> existingAtrOpt = atrrep.findById(atrmodel.getId());

        AmenitiesTypeRoom existingAtr = existingAtrOpt.get();

        // kiểm tra tên dịch vụ phòng
        if (atrmodel.getAmenitiesTypeRoomName() == null || atrmodel.getAmenitiesTypeRoomName().isEmpty()) {
            errorMessages.add("Tên tiện nghi loại phòng không được để trống");
        } else if (!existingAtr.getAmenitiesTypeRoomName().equals(atrmodel.getAmenitiesTypeRoomName()) && atrrep.existsByAmenitiesTypeRoomName(atrmodel.getAmenitiesTypeRoomName())) {
            errorMessages.add("Tên tiện nghi loại phòng này đã tồn tại");
        }


        // Nếu có lỗi, ném ngoại lệ với thông báo lỗi
        if (!errorMessages.isEmpty()) {
            throw new ValidationException(String.join(", ", errorMessages));
        }

        try {
            //Cập nhật các thuộc tính cho tài khoản
            existingAtr.setAmenitiesTypeRoomName(atrmodel.getAmenitiesTypeRoomName());
            AmenitiesTypeRoom updatedAtr = atrrep.save(existingAtr);
            return convertToDto(updatedAtr); // Chuyển đổi tài khoản đã lưu sang DTO

        } catch (DataIntegrityViolationException e) {
            // Xử lý lỗi vi phạm tính toàn vẹn dữ liệu (VD: trùng lặp dịch vụ phòng)
            throw new RuntimeException("Có lỗi xảy ra do vi phạm tính toàn vẹn dữ liệu", e);
        } catch (Exception e) {
            // Xử lý lỗi chung
            throw new RuntimeException("Có lỗi xảy ra khi cập nhật dịch vụ phòng", e);
        }
    }

    public void deleteAmenitiesTypeRoom(Integer id) {
        if (!atrrep.existsById(id)) {
            throw new NoSuchElementException("Dịch vụ phòng này không tồn tại"); // Ném ngoại lệ nếu không tồn tại
        }
        atrrep.deleteById(id);
    }


}
