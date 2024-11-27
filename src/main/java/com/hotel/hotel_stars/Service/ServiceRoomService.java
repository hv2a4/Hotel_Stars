package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.BookingRoomServiceRoomDto;
import com.hotel.hotel_stars.DTO.ServiceRoomDto;
import com.hotel.hotel_stars.DTO.TypeServiceRoomDto;
import com.hotel.hotel_stars.Entity.BookingRoomServiceRoom;
import com.hotel.hotel_stars.Entity.ServiceRoom;
import com.hotel.hotel_stars.Entity.TypeServiceRoom;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Models.serviceRoomModel;
import com.hotel.hotel_stars.Repository.ServiceRoomRepository;
import com.hotel.hotel_stars.Repository.TypeRoomServiceRepository;

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
    @Autowired
    TypeRoomServiceRepository typeRoomServiceRepository;
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
	
    // chuyển đổi entity sang dto (đổ dữ liệu lên web)
    public ServiceRoomDto convertToDto(ServiceRoom sr) {
        ServiceRoomDto serviceRoomDto = new ServiceRoomDto();
        List<BookingRoomServiceRoomDto> bookingRoomServiceRoomDtos = sr.getBookingRoomServiceRooms().stream().map(this::convertDto).toList();
        serviceRoomDto.setBookingRoomServiceRooms(bookingRoomServiceRoomDtos);
        serviceRoomDto.setId(sr.getId());
        serviceRoomDto.setImageName(sr.getImageName());
        serviceRoomDto.setPrice(sr.getPrice());
        serviceRoomDto.setServiceRoomName(sr.getServiceRoomName());
        TypeServiceRoomDto typeServiceRoom = new TypeServiceRoomDto();
        typeServiceRoom.setDuration(sr.getTypeServiceRoomId().getDuration());
        typeServiceRoom.setId(sr.getTypeServiceRoomId().getId());
        typeServiceRoom.setServiceRoomName(sr.getTypeServiceRoomId().getServiceRoomName());
        typeServiceRoom.setDuration(sr.getTypeServiceRoomId().getDuration());
        serviceRoomDto.setTypeServiceRoomDto(typeServiceRoom);
        return serviceRoomDto;
    }
//khôi
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

        try {
            ServiceRoom sr = new ServiceRoom();
            TypeServiceRoom typeServiceRoom = typeRoomServiceRepository.findById(srmodel.getTypeServiceRoom()).get();

            sr.setServiceRoomName(srmodel.getServiceRoomName());
            sr.setPrice(srmodel.getPrice());
            sr.setImageName(srmodel.getImageName());
            sr.setTypeServiceRoomId(typeServiceRoom);

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
        	TypeServiceRoom typeServiceRoom = typeRoomServiceRepository.findById(srmodel.getTypeServiceRoom()).get();
            existingServiceRoom.setServiceRoomName(srmodel.getServiceRoomName());
            existingServiceRoom.setPrice(srmodel.getPrice());
            existingServiceRoom.setImageName(srmodel.getImageName());
            existingServiceRoom.setTypeServiceRoomId(typeServiceRoom);
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
    
    public List<ServiceRoomDto> getServiceRoomsByBookingRoomId(Integer bookingRoomId) {
    	List<ServiceRoom> serviceRoom = srrep.findServiceRoomsByBookingRoomId(bookingRoomId);
        return serviceRoom.stream().map(this::convertToDto).toList();
    }

}
