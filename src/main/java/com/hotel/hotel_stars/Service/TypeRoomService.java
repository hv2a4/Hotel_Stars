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
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

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

        // Kiểm tra giá
        if (trmodel.getPrice() == null) {
            errorMessages.add("Giá không được để trống");
        } else if (!isValidPrice(trmodel.getPrice())) {
            errorMessages.add("Giá bạn nhập không hợp lệ");
        }

        // Kiểm tra loại giường
        if (trmodel.getBedType() == null || trmodel.getBedType().isEmpty()) {
            errorMessages.add("Loại giường không được để trống");
        }

        // Kiểm tra số lượng giường
        if (trmodel.getBedCount() == null) {
            errorMessages.add("Số lượng giường không được để trống");
        } else if (!isValidBedCount(trmodel.getBedCount())) {
            errorMessages.add("Số lượng giường bạn nhập không hợp lệ");
        }

        // Kiểm tra diện tích
        if (trmodel.getAcreage() == null) {
            errorMessages.add("Diện tích không được để trống");
        } else if (!isValidAcreage(trmodel.getAcreage())) {
            errorMessages.add("Diện tích bạn nhập không hợp lệ");
        }

        // Kiểm tra giới hạn số lượng khách
        if (trmodel.getGuestLimit() == null) {
            errorMessages.add("Giới hạn số lượng khách không được để trống");
        }

        try {
            TypeRoom typeRoom = new TypeRoom();

            // Đặt thông tin loại phòng
            typeRoom.setTypeRoomName(trmodel.getTypeRoomName());
            typeRoom.setPrice(trmodel.getPrice());
            typeRoom.setBedCount(trmodel.getBedCount());
            typeRoom.setAcreage(trmodel.getAcreage());
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

        TypeRoom existingTypeRoom = existingTypeRoomOpt.get();

        // Kiểm tra tên loại phòng
        if (trModel.getTypeRoomName() == null || trModel.getTypeRoomName().isEmpty()) {
            errorMessages.add("Tên loại phòng không được để trống");
        } else if (!existingTypeRoom.getTypeRoomName().equals(trModel.getTypeRoomName()) && typeRoomRepository.existsByTypeRoomName(trModel.getTypeRoomName())) {
            errorMessages.add("Tên loại phòng này đã tồn tại");
        }

        // Kiểm tra giá
        if (trModel.getPrice() == null) {
            errorMessages.add("Giá không được để trống");
        } else if (!isValidPrice(trModel.getPrice())) {
            errorMessages.add("Giá bạn nhập không hợp lệ");
        }

        // Kiểm tra loại giường
        if (trModel.getBedType() == null || trModel.getBedType().isEmpty()) {
            errorMessages.add("Loại giường không được để trống");
        }

        // Kiểm tra số lượng giường
        if (trModel.getBedCount() == null) {
            errorMessages.add("Số lượng giường không được để trống");
        } else if (!isValidBedCount(trModel.getBedCount())) {
            errorMessages.add("Số lượng giường không hợp lệ");
        }

        // Kiểm tra diện tích
        if (trModel.getAcreage() == null) {
            errorMessages.add("Diện tích không được để trống");
        } else if (!isValidAcreage(trModel.getAcreage())) {
            errorMessages.add("Diện tích không hợp lệ");
        }

        // Kiểm tra giới hạn khách
        if (trModel.getGuestLimit() == null) {
            errorMessages.add("Giới hạn số khách không được để trống");
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
    public void deleteServiceRoom(Integer id) {
        if (!typeRoomRepository.existsById(id)) {
            throw new NoSuchElementException("Loại phòng phòng này không tồn tại"); // Ném ngoại lệ nếu không tồn tại
        }
        typeRoomRepository.deleteById(id);
    }

    // checkValidation cho các trường dữ liệu
    private boolean isValidPrice(Double price) {
        // Kiểm tra xem giá có null hay không
        if (price == null) {
            return false; // không được để trống
        }

        // Kiểm tra xem giá có lớn hơn 0 hay không
        if (price <= 0) {
            return false; // Giá phải lớn hơn 0
        }

        // Kiểm tra xem số có hợp lệ hay không
        String priceStr = price.toString();
        // Nếu giá không phải là một số hợp lệ (chỉ chứa số và có thể có dấu phẩy)
        if (!priceStr.matches("^[0-9]+(\\.[0-9]{1,2})?$")) {
            return false; // số không hợp lệ
        }

        return true; // Nếu tất cả các kiểm tra đều hợp lệ
    }

    private boolean isValidBedCount(Integer bedCount) {
        // Kiểm tra xem giá có null hay không
        if (bedCount == null) {
            return false; // không được để trống
        }

        // Kiểm tra xem giá có lớn hơn 0 hay không
        if (bedCount <= 0) {
            return false; // Giá phải lớn hơn 0
        }

        // Kiểm tra xem số có hợp lệ hay không
        String bedCountStr = bedCount.toString();
        // Nếu giá không phải là một số hợp lệ (chỉ chứa số và có thể có dấu phẩy)
        if (!bedCountStr.matches("^[0-9]+(\\.[0-9]{1,2})?$")) {
            return false; // số không hợp lệ
        }

        return true; // Nếu tất cả các kiểm tra đều hợp lệ
    }

    private boolean isValidAcreage(Double acreage) {
        // Kiểm tra xem giá có null hay không
        if (acreage == null) {
            return false; // không được để trống
        }

        // Kiểm tra xem giá có lớn hơn 0 hay không
        if (acreage <= 0) {
            return false; // Giá phải lớn hơn 0
        }

        // Kiểm tra xem số có hợp lệ hay không
        String acreageStr = acreage.toString();
        // Nếu giá không phải là một số hợp lệ (chỉ chứa số và có thể có dấu phẩy)
        if (!acreageStr.matches("^[0-9]+(\\.[0-9]{1,2})?$")) {
            return false; // số không hợp lệ
        }

        return true; // Nếu tất cả các kiểm tra đều hợp lệ
    }

    private boolean isValidGuestLimit(Integer guestLimit) {
        // Kiểm tra xem giá có null hay không
        if (guestLimit == null) {
            return false; // không được để trống
        }

        // Kiểm tra xem giá có lớn hơn 0 hay không
        if (guestLimit <= 0) {
            return false; // Giá phải lớn hơn 0
        }

        // Kiểm tra xem số có hợp lệ hay không
        String guestLimitStr = guestLimit.toString();
        // Nếu giá không phải là một số hợp lệ (chỉ chứa số và có thể có dấu phẩy)
        if (!guestLimitStr.matches("^[0-9]+(\\.[0-9]{1,2})?$")) {
            return false; // số không hợp lệ
        }

        return true; // Nếu tất cả các kiểm tra đều hợp lệ
    }

    public List<TypeRoomDto> getTypeRooms() {
        List<TypeRoom> list = typeRoomRepository.findTop3TypeRooms();
        return list.stream().map(this::convertTypeRoomDto).toList();
    }

}
