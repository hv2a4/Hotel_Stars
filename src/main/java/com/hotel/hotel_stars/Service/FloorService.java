package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.FloorDto;
import com.hotel.hotel_stars.DTO.HotelDto;
import com.hotel.hotel_stars.Entity.Floor;
import com.hotel.hotel_stars.Entity.Hotel;
import com.hotel.hotel_stars.Models.floorModel;
import com.hotel.hotel_stars.Repository.FloorRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FloorService {
    @Autowired
    private FloorRepository floorrep;

//    public HotelDto convertHotelToHotelDto(Hotel hotel) {
//        return new HotelDto(
//                hotel.getId(),
//                hotel.getHotelName(),
//                hotel.getDescriptions(),
//                hotel.getProvince(),
//                hotel.getWard(),
//                hotel.getAddress()
//        );
//    }

    public FloorDto convertToDto(Floor fl) {
        return new FloorDto(
                fl.getId(),
                fl.getFloorName()
        );
    }

    public List<FloorDto> getAllFloors() {
        List<Floor> fls = floorrep.findAll();
        return fls.stream()
                .map(this::convertToDto)
                .toList();
    }

    public FloorDto addFloor(floorModel flmodel) {
        List<String> errorMessages = new ArrayList<>(); // Danh sách lưu trữ các thông báo lỗi

        // Kiểm tra tên
        if (flmodel.getFloorName() == null || flmodel.getFloorName().isEmpty()) {
            errorMessages.add("Tên dịch vụ phòng không được để trống");
        } else if (floorrep.existsByFloorName(flmodel.getFloorName())) {
            errorMessages.add("Tên này đã tồn tại");
        }

        if (!errorMessages.isEmpty()) {
            throw new RuntimeException(String.join(", ", errorMessages));
        }

        try {
            Floor fl = new Floor();
            // In ra màn hình
            System.out.println("ID: " + fl.getId());
            System.out.println("Tên loại tiện phòng: " + fl.getFloorName());

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

    public FloorDto updateFloor(Integer flId, floorModel flmodel) {
        List<String> errorMessages = new ArrayList<>(); // Danh sách lưu trữ các thông báo lỗi

        // Kiểm tra xem tài khoản có tồn tại hay không
        Optional<Floor> existingFlOpt = floorrep.findById(flId);

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

    public void deleteAmenitiesTypeRoom(Integer id) {
        if (!floorrep.existsById(id)) {
            throw new NoSuchElementException("Dịch vụ phòng này không tồn tại"); // Ném ngoại lệ nếu không tồn tại
        }
        floorrep.deleteById(id);
    }
}
