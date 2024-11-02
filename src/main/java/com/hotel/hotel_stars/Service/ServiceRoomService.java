package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.ServiceRoomDto;
import com.hotel.hotel_stars.Entity.ServiceRoom;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Models.serviceRoomModel;
import com.hotel.hotel_stars.Repository.ServiceRoomRepository;
import jakarta.validation.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class ServiceRoomService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ServiceRoomRepository srrep;

    // chuyển đổi entity sang dto (đổ dữ liệu lên web)
    public ServiceRoomDto convertToDto(ServiceRoom sr) {
        return new ServiceRoomDto(
                sr.getId(),
                sr.getServiceRoomName(),
                sr.getPrice(),
                sr.getImageName());
    }

    // Hiển thị danh sách dịch vụ phòng
    public List<ServiceRoomDto> getAllServiceRooms() {
        List<ServiceRoom> srs = srrep.findAll();
        return srs.stream()
                .map(this::convertToDto)
                .toList();
    }

    // thêm service room
    public ServiceRoomDto addServiceRoom(serviceRoomModel srmodel) {
        List<String> errorMessages = new ArrayList<>(); // Danh sách lưu trữ các thông báo lỗi

        // Kiểm tra tên dịch vụ phòng
        if (srmodel.getServiceRoomName() == null || srmodel.getServiceRoomName().isEmpty()) {
            errorMessages.add("Tên dịch vụ phòng không được để trống");
        } else if (srrep.existsByServiceRoomName(srmodel.getServiceRoomName())) {
            errorMessages.add("Dịch vụ phòng này đã tồn tại");
        }

        // Kiểm tra đơn giá
        if (srmodel.getPrice() == null) {
            errorMessages.add("Giá không được để trống");
        } else if (!isValidPrice(srmodel.getPrice())) {
            errorMessages.add("Giá bạn nhập không hợp lệ");
        }

        // Kiểm tra hình ảnh
        if (srmodel.getImageName() == null || srmodel.getImageName().isEmpty()) {
            errorMessages.add("Hình ảnh không được để trống");
        }

        // Nếu có lỗi, ném ngoại lệ với thông báo lỗi
        if (!errorMessages.isEmpty()) {
            throw new CustomValidationException(errorMessages); // Ném ngoại lệ tùy chỉnh
        }

        try {
            ServiceRoom sr = new ServiceRoom();
            // In ra màn hình
            System.out.println("Tên dịch vụ phòng: " + srmodel.getServiceRoomName());
            System.out.println("Giá phòng: " + srmodel.getPrice());
            System.out.println("Hình ảnh: " + srmodel.getImageName());

            sr.setServiceRoomName(srmodel.getServiceRoomName());
            sr.setPrice(srmodel.getPrice());
            sr.setImageName("https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F3c7db4be-6f94-4c19-837e-fbfe8848546f?alt=media&token=2aed7a07-6850-4c82-ae7a-5ee1ba606979");

            // Lưu tài khoản vào cơ sở dữ liệu và chuyển đổi sang DTO
            ServiceRoom savedServiceRoom = srrep.save(sr);
            return convertToDto(savedServiceRoom);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Có lỗi xảy ra do vi phạm tính toàn vẹn dữ liệu", e);
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra khi thêm dịch vụ phòng!", e);
        }
    }

    // cập nhật service room
    public ServiceRoomDto updateServiceRoom(Integer srId, serviceRoomModel srmodel) {
        List<String> errorMessages = new ArrayList<>(); // Danh sách lưu trữ các thông báo lỗi

        // Kiểm tra xem tài khoản có tồn tại hay không
        Optional<ServiceRoom> existingServiveRoomOpt = srrep.findById(srId);
        if (!existingServiveRoomOpt.isPresent()) {
            throw new CustomValidationException(List.of("Tên dịch vụ phòng không tồn tại"));
        }

        ServiceRoom existingServiceRoom = existingServiveRoomOpt.get();

        // kiểm tra tên dịch vụ phòng
        if (srmodel.getServiceRoomName() == null || srmodel.getServiceRoomName().isEmpty()) {
            errorMessages.add("Tên dịch vụ phòng không được để trống");
        } else if (!existingServiceRoom.getServiceRoomName().equals(srmodel.getServiceRoomName()) && srrep.existsByServiceRoomName(srmodel.getServiceRoomName())) {
            errorMessages.add("Dịch vụ của phòng này đã tồn tại");
        }

        // kiểm tra đơn giá
        if (srmodel.getPrice() == null) {
            errorMessages.add("Giá không được để trống");
        } else if (!isValidPrice(srmodel.getPrice())) {
            errorMessages.add("Giá bạn nhập không hợp lệ");
        }

        // kiểm tra hình ảnh
        if (srmodel.getImageName() == null || srmodel.getImageName().isEmpty()) {
            errorMessages.add("Hình ảnh không được để trống");
        }

        // Nếu có lỗi, ném ngoại lệ với thông báo lỗi
        if (!errorMessages.isEmpty()) {
            throw new ValidationException(String.join(", ", errorMessages));
        }

        try {
            //Cập nhật các thuộc tính cho tài khoản
            existingServiceRoom.setServiceRoomName(srmodel.getServiceRoomName());
            existingServiceRoom.setPrice(srmodel.getPrice());
            existingServiceRoom.setImageName("https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F3c7db4be-6f94-4c19-837e-fbfe8848546f?alt=media&token=2aed7a07-6850-4c82-ae7a-5ee1ba606979");
            // Lưu tài khoản đã cập nhật vào cơ sở dữ liệu và chuyển đổi sang DTO
            ServiceRoom updatedServiceRoom = srrep.save(existingServiceRoom);
            return convertToDto(updatedServiceRoom); // Chuyển đổi tài khoản đã lưu sang DTO

        } catch (DataIntegrityViolationException e) {
            // Xử lý lỗi vi phạm tính toàn vẹn dữ liệu (VD: trùng lặp dịch vụ phòng)
            throw new RuntimeException("Có lỗi xảy ra do vi phạm tính toàn vẹn dữ liệu", e);
        } catch (Exception e) {
            // Xử lý lỗi chung
            throw new RuntimeException("Có lỗi xảy ra khi cập nhật dịch vụ phòng", e);
        }
    }

    // xóa service room
    public void deleteServiceRoom(Integer id) {
        if (!srrep.existsById(id)) {
            throw new NoSuchElementException("Dịch vụ phòng này không tồn tại"); // Ném ngoại lệ nếu không tồn tại
        }
        srrep.deleteById(id);
    }

    // phương thức đơn giá
    private boolean isValidPrice(Double price) {
        // Kiểm tra xem giá có null hay không
        if (price == null) {
            return false; // Giá không được để trống
        }

        // Kiểm tra xem giá có lớn hơn 0 hay không
        if (price <= 0) {
            return false; // Giá phải lớn hơn 0
        }

        // Kiểm tra xem giá có phải là số hợp lệ không
        String priceStr = price.toString();
        // Nếu giá không phải là một số hợp lệ (chỉ chứa số và có thể có dấu phẩy)
        if (!priceStr.matches("^[0-9]+(\\.[0-9]{1,2})?$")) {
            return false; // Giá không hợp lệ
        }

        return true; // Nếu tất cả các kiểm tra đều hợp lệ
    }


}
