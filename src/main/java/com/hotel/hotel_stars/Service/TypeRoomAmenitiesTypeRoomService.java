package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.AmenitiesTypeRoomDto;
import com.hotel.hotel_stars.DTO.TypeBedDto;
import com.hotel.hotel_stars.DTO.TypeRoomAmenitiesTypeRoomDto;
import com.hotel.hotel_stars.DTO.TypeRoomDto;
import com.hotel.hotel_stars.Entity.*;
import com.hotel.hotel_stars.Models.TypeRoomAmenitiesTypeRoomModel;
import com.hotel.hotel_stars.Repository.AmenitiesTypeRoomRepository;
import com.hotel.hotel_stars.Repository.TypeRoomAmenitiesTypeRoomRepository;
import com.hotel.hotel_stars.Repository.TypeRoomRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
    public class TypeRoomAmenitiesTypeRoomService {
    @Autowired
    TypeRoomAmenitiesTypeRoomRepository tr_atr_rep;

    @Autowired
    TypeRoomRepository trrep;

    @Autowired
    AmenitiesTypeRoomRepository atrrep;

    // Lấy danh sách tiện nghi theo typeRoomId
    public List<TypeRoomAmenitiesTypeRoomDto> getAmenitiesByTypeRoomId(Integer typeRoomId) {
        List<TypeRoomAmenitiesTypeRoom> tr_atrs = tr_atr_rep.findByTypeRoom_Id(typeRoomId);
        return tr_atrs.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public TypeRoomDto convertTypeRoomDto(TypeRoom tr) {
        TypeBedDto typeBedDto = new TypeBedDto();
        typeBedDto.setId(tr.getTypeBed().getId());
        typeBedDto.setBedName(tr.getTypeBed().getBedName());
        return new TypeRoomDto(
                tr.getId(),
                tr.getTypeRoomName(),
                tr.getPrice(),
                tr.getBedCount(),
                tr.getAcreage(),
                tr.getGuestLimit(),
                typeBedDto,
                tr.getDescribes(),
                null
        );
    }

    public AmenitiesTypeRoomDto convertAmenitiesTypeRoomToDto(AmenitiesTypeRoom atr) {
        return new AmenitiesTypeRoomDto(
                atr.getId(),
                atr.getAmenitiesTypeRoomName()
//                atr.getIcon()
        );
    }

    public TypeRoomAmenitiesTypeRoomDto convertToDto(TypeRoomAmenitiesTypeRoom tr_atr) {
        TypeRoomDto typeRoomDto = convertTypeRoomDto(tr_atr.getTypeRoom());
        AmenitiesTypeRoomDto amenitiesTypeRoomDto = convertAmenitiesTypeRoomToDto(tr_atr.getAmenitiesTypeRoom());
        return new TypeRoomAmenitiesTypeRoomDto(
                tr_atr.getId(),
                typeRoomDto,
                amenitiesTypeRoomDto
        );
    }

    public List<TypeRoomAmenitiesTypeRoomDto> getAllTrAtr() {
        List<TypeRoomAmenitiesTypeRoom> tr_atrs = tr_atr_rep.findAll();
        return tr_atrs.stream()
                .map(this::convertToDto)
                .toList();
    }

    // thêm dịch vụ phòng
//    public TypeRoomAmenitiesTypeRoomDto addTypeRoomAmenitiesTypeRoom(TypeRoomAmenitiesTypeRoomModel tr_atrmodel) {
//        try {
//            TypeRoomAmenitiesTypeRoom tr_atr = new TypeRoomAmenitiesTypeRoom();
//
//            // Đặt thông tin loại phòng
//            Optional<TypeRoom> tr = trrep.findById(tr_atrmodel.getTypeRoomId());
//            if (tr.isEmpty()) {
//                throw new ValidationException("Loại phòng với ID " + tr_atrmodel.getTypeRoomId() + " không tồn tại.");
//            }
//            tr_atr.setTypeRoom(tr.get());
//
//            Optional<AmenitiesTypeRoom> atr = atrrep.findById(tr_atrmodel.getAmenitiesTypeRoomId());
//            if (atr.isEmpty()) {
//                throw new ValidationException("Tiện nghi phòng với ID " + tr_atrmodel.getAmenitiesTypeRoomId() + " không tồn tại.");
//            }
//            tr_atr.setAmenitiesTypeRoom(atr.get());
//
//            // Lưu thông tin loại phòng vào cơ sở dữ liệu
//            TypeRoomAmenitiesTypeRoom savedTrAtr = tr_atr_rep.save(tr_atr);
//
//            // Chuyển đổi và trả về DTO
//            return convertToDto(savedTrAtr);
//        } catch (DataIntegrityViolationException e) {
//            throw new RuntimeException("Có lỗi xảy ra do vi phạm tính toàn vẹn dữ liệu", e);
//        } catch (ValidationException e) {
//            throw new RuntimeException("Dữ liệu không hợp lệ: " + e.getMessage(), e);
//        } catch (Exception e) {
//            throw new RuntimeException("Có lỗi xảy ra khi thêm vào!", e);
//        }
//    }

    // Cập nhật thông tin dịch vụ phòng
//    public TypeRoomAmenitiesTypeRoomDto updateTypeRoomAmenitiesTypeRoom(Integer id, TypeRoomAmenitiesTypeRoomModel tr_atrModel) {
//        try {
//            Optional<TypeRoomAmenitiesTypeRoom> existingTrAtr = tr_atr_rep.findById(id);
//            if (existingTrAtr.isEmpty()) {
//                throw new ValidationException("Dịch vụ phòng với ID " + id + " không tồn tại.");
//            }
//
//            TypeRoomAmenitiesTypeRoom tr_atr = existingTrAtr.get();
//
//            // Kiểm tra và cập nhật loại phòng
//            Optional<TypeRoom> tr = trrep.findById(tr_atrModel.getTypeRoomId());
//            if (tr.isEmpty()) {
//                throw new ValidationException("Loại phòng với ID " + tr_atrModel.getTypeRoomId() + " không tồn tại.");
//            }
//            tr_atr.setTypeRoom(tr.get());
//
//            // Kiểm tra và cập nhật tiện nghi phòng
//            Optional<AmenitiesTypeRoom> atr = atrrep.findById(tr_atrModel.getAmenitiesTypeRoomId());
//            if (atr.isEmpty()) {
//                throw new ValidationException("Tiện nghi phòng với ID " + tr_atrModel.getAmenitiesTypeRoomId() + " không tồn tại.");
//            }
//            tr_atr.setAmenitiesTypeRoom(atr.get());
//
//            // Lưu thông tin cập nhật vào cơ sở dữ liệu
//            TypeRoomAmenitiesTypeRoom updatedTrAtr = tr_atr_rep.save(tr_atr);
//
//            // Chuyển đổi và trả về DTO
//            return convertToDto(updatedTrAtr);
//        } catch (ValidationException e) {
//            throw new RuntimeException("Dữ liệu không hợp lệ: " + e.getMessage(), e);
//        } catch (Exception e) {
//            throw new RuntimeException("Có lỗi xảy ra khi cập nhật!", e);
//        }
//    }

    // Xóa dịch vụ phòng
    public void deleteTypeRoomAmenitiesTypeRoom(Integer id) {
        try {
            if (!tr_atr_rep.existsById(id)) {
                throw new ValidationException("Dịch vụ phòng với ID " + id + " không tồn tại.");
            }
            tr_atr_rep.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra khi xóa!", e);
        }
    }

}
