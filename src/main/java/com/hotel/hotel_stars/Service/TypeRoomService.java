package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.Select.TypeRoomBookingCountDto;
import com.hotel.hotel_stars.DTO.TypeBedDto;
import com.hotel.hotel_stars.DTO.TypeRoomDto;
import com.hotel.hotel_stars.DTO.selectDTO.FindTypeRoomDto;
import com.hotel.hotel_stars.Entity.TypeBed;
import com.hotel.hotel_stars.Entity.TypeRoom;
import com.hotel.hotel_stars.Models.typeRoomModel;
import com.hotel.hotel_stars.Repository.TypeBedRepository;
import com.hotel.hotel_stars.Repository.TypeRoomRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

@Service
public class TypeRoomService {
    @Autowired
    TypeRoomRepository typeRoomRepository;

    @Autowired
    TypeBedRepository typeBedRepository;

    // Tìm kiếm loại phòng
    public List<FindTypeRoomDto> getFindTypeRoom() {
        LocalDate startDate = LocalDate.parse("2023-10-29");
        LocalDate endDate = LocalDate.parse("2023-10-31");
        List<Object[]> results = typeRoomRepository.findAllTypeRoomDetailsWithCost(startDate, endDate);
        List<FindTypeRoomDto> dtoList = new ArrayList<>();
        results.stream().forEach(row -> {
            String typeRoomName = (String) row[0];
            Double price = (Double) row[1];
            Double acreage = (Double) row[2];
            Integer guestLimit = (Integer) row[3];
            String amenitiesTypeRoomName = (String) row[4];
            Double estCost = (Double) row[5];
            String image = (String) row[6];
            // Kiểm tra xem DTO đã tồn tại trong danh sách chưa bằng Stream API
            FindTypeRoomDto existingDto = dtoList.stream().filter(dto -> dto.getTypeRoomName().equals(typeRoomName)).findFirst().orElse(null);

            if (existingDto == null) {
                // Nếu chưa có DTO cho loại phòng này, tạo mới
                existingDto = new FindTypeRoomDto(typeRoomName, price, acreage, guestLimit, new ArrayList<>(), estCost, image);
                dtoList.add(existingDto);
            }
            existingDto.getAmenitiesTypeRoomNames().add(amenitiesTypeRoomName);
        });
        return dtoList; // Trả về danh sách DTO
    }

    // chuyển đổi entity sang dto (đổ dữ liệu lên web)
    public TypeRoomDto convertTypeRoomDto(TypeRoom tr) {
        TypeBedDto typeBedDto = new TypeBedDto();
        typeBedDto.setId(tr.getTypeBed().getId());
        typeBedDto.setBedName(tr.getTypeBed().getBedName());
        return new TypeRoomDto(tr.getId(), tr.getTypeRoomName(), tr.getPrice(), tr.getBedCount(), tr.getAcreage(), tr.getGuestLimit(), typeBedDto);
    }

    // Hiển thị danh sách dịch vụ phòng
    public List<TypeRoomDto> getAllTypeRooms() {
        List<TypeRoom> trs = typeRoomRepository.findAll();
        return trs.stream().map(this::convertTypeRoomDto).toList();
    }

    // thêm dịch vụ phòng
    public TypeRoomDto addTypeRoom(typeRoomModel trmodel) {
        List<String> errorMessages = new ArrayList<>(); // Danh sách lưu trữ các thông báo lỗi

        // Kiểm tra tên loại phòng
        if (trmodel.getTypeRoomName() == null || trmodel.getTypeRoomName().isEmpty()) {
            errorMessages.add("Tên dịch vụ phòng không được để trống");
        } else if (typeRoomRepository.existsByTypeRoomName(trmodel.getTypeRoomName())) {
            errorMessages.add("Dịch vụ phòng này đã tồn tại");
        }

        if (trmodel.getPrice() == null) {
            errorMessages.add("Giá không được để trống");
        } else if (trmodel.getPrice().doubleValue() <= 0) {
            errorMessages.add("Giá bạn nhập không hợp lệ");
        }

        // Kiểm tra số lượng giường
        if (trmodel.getBedCount() == null) {
            errorMessages.add("Số lượng giường không được để trống");
        } else if (trmodel.getBedCount() <= 0) {
            errorMessages.add("Số lượng giường phải là số dương");
        }

        // Kiểm tra diện tích
        if (trmodel.getAcreage() == null) {
            errorMessages.add("Diện tích phòng không được để trống");
        } else if (trmodel.getAcreage().doubleValue() <= 0) {
            errorMessages.add("Diện tích phòng phải là số dương");
        }

        if (trmodel.getGuestLimit() == null || trmodel.getGuestLimit().isEmpty()) {
            errorMessages.add("Giới hạn khách không được để trống");
        }

        if (trmodel.getTypeBedId() == null) {
            errorMessages.add("ID loại giường không được để trống");
        }

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(String.join(", ", errorMessages));
        }

        try {
            TypeRoom typeRoom = new TypeRoom();

            // Đặt thông tin loại phòng
            typeRoom.setTypeRoomName(trmodel.getTypeRoomName());
            typeRoom.setPrice(trmodel.getPrice());
            typeRoom.setBedCount(trmodel.getBedCount());
            typeRoom.setAcreage(trmodel.getAcreage());
            Optional<TypeBed> typeBed = typeBedRepository.findById(trmodel.getTypeBedId());
            typeRoom.setTypeBed(typeBed.get());
            typeRoom.setGuestLimit(String.valueOf(trmodel.getGuestLimit()));
            // Lưu thông tin loại phòng vào cơ sở dữ liệu và chuyển đổi sang DTO
            TypeRoom savedTypeRoom = typeRoomRepository.save(typeRoom);
            return convertTypeRoomDto(savedTypeRoom);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Có lỗi xảy ra do vi phạm tính toàn vẹn dữ liệu", e);
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra khi thêm loại phòng!", e);
        }
    }

    // cập nhật dịch vụ phòng
    public TypeRoomDto updateTypeRoom(Integer trId, typeRoomModel trModel) {
        List<String> errorMessages = new ArrayList<>(); // Danh sách lưu trữ các thông báo lỗi

        // Kiểm tra xem loại phòng có tồn tại hay không
        Optional<TypeRoom> existingTypeRoomOpt = typeRoomRepository.findById(trId);
        if (!existingTypeRoomOpt.isPresent()) {
            throw new EntityNotFoundException("Loại phòng với ID " + trId + " không tồn tại.");
        }
        TypeRoom existingTypeRoom = existingTypeRoomOpt.get();

        // Kiểm tra tên loại phòng
        if (trModel.getTypeRoomName() == null || trModel.getTypeRoomName().isEmpty()) {
            errorMessages.add("Tên loại phòng không được để trống");
        } else if (!existingTypeRoom.getTypeRoomName().equals(trModel.getTypeRoomName()) && typeRoomRepository.existsByTypeRoomName(trModel.getTypeRoomName())) {
            errorMessages.add("Tên loại phòng này đã tồn tại");
        }

        if (trModel.getPrice() == null) {
            errorMessages.add("Giá không được để trống");
        } else if (trModel.getPrice().doubleValue() <= 0) {
            errorMessages.add("Giá bạn nhập không hợp lệ");
        }

        // Kiểm tra số lượng giường
        if (trModel.getBedCount() == null) {
            errorMessages.add("Số lượng giường không được để trống");
        } else if (trModel.getBedCount() <= 0) {
            errorMessages.add("Số lượng giường phải là số dương");
        }

        // Kiểm tra diện tích
        if (trModel.getAcreage() == null) {
            errorMessages.add("Diện tích phòng không được để trống");
        } else if (trModel.getAcreage().doubleValue() <= 0) {
            errorMessages.add("Diện tích phòng phải là số dương");
        }

        if (trModel.getGuestLimit() == null || trModel.getGuestLimit().isEmpty()) {
            errorMessages.add("Giới hạn khách không được để trống");
        }

        if (trModel.getTypeBedId() == null) {
            errorMessages.add("ID loại giường không được để trống");
        }

        // Nếu có lỗi, ném ngoại lệ với thông báo lỗi
        if (!errorMessages.isEmpty()) {
            throw new ValidationException(String.join(", ", errorMessages));
        }

        try {
            // Cập nhật các thuộc tính cho loại phòng
            existingTypeRoom.setTypeRoomName(trModel.getTypeRoomName());
            existingTypeRoom.setPrice(trModel.getPrice());
            existingTypeRoom.setBedCount(trModel.getBedCount());
            existingTypeRoom.setAcreage(trModel.getAcreage());
            Optional<TypeBed> typeBed = typeBedRepository.findById(trModel.getTypeBedId());
            existingTypeRoom.setTypeBed(typeBed.get());
            existingTypeRoom.setGuestLimit(String.valueOf(trModel.getGuestLimit()));

            // Lưu loại phòng đã cập nhật vào cơ sở dữ liệu và chuyển đổi sang DTO
            TypeRoom updatedTypeRoom = typeRoomRepository.save(existingTypeRoom);
            return convertTypeRoomDto(updatedTypeRoom); // Chuyển đổi loại phòng đã lưu sang DTO

        } catch (DataIntegrityViolationException e) {
            // Xử lý lỗi vi phạm tính toàn vẹn dữ liệu
            throw new RuntimeException("Có lỗi xảy ra do vi phạm tính toàn vẹn dữ liệu", e);
        } catch (Exception e) {
            // Xử lý lỗi chung
            throw new RuntimeException("Có lỗi xảy ra khi cập nhật loại phòng", e);
        }
    }

    // xóa dịch vụ phòng
    public void deleteTypeRoom(Integer id) {
        if (!typeRoomRepository.existsById(id)) {
            throw new NoSuchElementException("Loại phòng phòng này không tồn tại"); // Ném ngoại lệ nếu không tồn tại
        }
        typeRoomRepository.deleteById(id);
    }


    public List<TypeRoomDto> getTypeRooms() {
        List<TypeRoom> list = typeRoomRepository.findTop3TypeRooms();
        return list.stream().map(this::convertTypeRoomDto).toList();
    }

}
