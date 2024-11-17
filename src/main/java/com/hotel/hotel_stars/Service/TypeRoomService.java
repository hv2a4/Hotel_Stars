package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.*;
import com.hotel.hotel_stars.DTO.selectDTO.FindTypeRoomDto;
import com.hotel.hotel_stars.Entity.*;
import com.hotel.hotel_stars.Models.TypeRoomAmenitiesTypeRoomModel;
import com.hotel.hotel_stars.Models.amenitiesTypeRoomModel;
import com.hotel.hotel_stars.Models.typeRoomModel;
import com.hotel.hotel_stars.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class TypeRoomService {
    @Autowired
    TypeRoomRepository typeRoomRepository;

    @Autowired
    TypeBedRepository typeBedRepository;

    @Autowired
    TypeRoomImageRepository typeRoomImageRepository;

    @Autowired
    AmenitiesTypeRoomRepository amenitiesTypeRoomRepository;

    @Autowired
    TypeRoomAmenitiesTypeRoomRepository typeRoomAmenitiesTypeRoomRepository;

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
        List<TypeRoomImage> typeRoomImages = tr.getTypeRoomImages();

        List<TypeRoomImageDto> typeRoomImageDtos = new ArrayList<>();

        for (TypeRoomImage typeRoomImage : typeRoomImages) {
            TypeRoomImageDto typeRoomImageDto = new TypeRoomImageDto();
            typeRoomImageDto.setId(typeRoomImage.getId());  // Lấy ID của từng ảnh
            typeRoomImageDto.setImageName(typeRoomImage.getImageName());  // Lấy tên ảnh từ từng ảnh

            typeRoomImageDtos.add(typeRoomImageDto);  // Thêm vào danh sách DTO
        }
        return new TypeRoomDto(tr.getId(), tr.getTypeRoomName(), tr.getPrice(), tr.getBedCount(),
                tr.getAcreage(), tr.getGuestLimit(), typeBedDto,tr.getDescribes(), typeRoomImageDtos);
    }
    

    // Hiển thị danh sách dịch vụ phòng
    public List<TypeRoomDto> getAllTypeRooms() {
        List<TypeRoom> trs = typeRoomRepository.findAll();

        return trs.stream().map(this::convertTypeRoomDto).toList();
    }

    // thêm loại phòng
    public TypeRoomDto addTypeRoom(typeRoomModel trmodel) {
        List<String> errorMessages = new ArrayList<>();

        if (trmodel.getImageNames().length == 0) {
            throw new RuntimeException("422");
        }

        // Kiểm tra tên loại phòng đã tồn tại chưa
        Optional<TypeRoom> existingTypeRoom = typeRoomRepository.findByTypeRoomName(trmodel.getTypeRoomName());
        if (existingTypeRoom.isPresent()) {
            throw new RuntimeException("409");
        }

        TypeRoom typeRoom = new TypeRoom();
        typeRoom.setTypeRoomName(trmodel.getTypeRoomName());
        typeRoom.setPrice(trmodel.getPrice());
        typeRoom.setBedCount(trmodel.getBedCount());
        typeRoom.setAcreage(trmodel.getAcreage());

        Optional<TypeBed> typeBed = typeBedRepository.findById(trmodel.getTypeBedId());
        typeRoom.setTypeBed(typeBed.get());
        typeRoom.setGuestLimit(trmodel.getGuestLimit());
        typeRoom.setDescribes(trmodel.getDescribes());

        List<TypeRoomImage> typeRoomImages = new ArrayList<>();
        typeRoom.setTypeRoomImages(typeRoomImages);

        // Lưu thông tin loại phòng vào cơ sở dữ liệu
        TypeRoom savedTypeRoom = typeRoomRepository.save(typeRoom);

        //Lưu thông tin tiện nghi loại phòng
        List<amenitiesTypeRoomModel> amenitiesTypeRoomModel = trmodel.getAmenitiesTypeRooms();
        for (amenitiesTypeRoomModel item : amenitiesTypeRoomModel) {
            TypeRoomAmenitiesTypeRoom typeRoomAmenitiesTypeRoom = new TypeRoomAmenitiesTypeRoom();
            typeRoomAmenitiesTypeRoom.setTypeRoom(savedTypeRoom);
            Optional<AmenitiesTypeRoom> optional = amenitiesTypeRoomRepository.findById(item.getId());
            typeRoomAmenitiesTypeRoom.setAmenitiesTypeRoom(optional.get());
            typeRoomAmenitiesTypeRoomRepository.save(typeRoomAmenitiesTypeRoom);
        }

        // Lưu hình ảnh vào bảng TypeRoomImage
        if (trmodel.getImageNames() != null) {
            for (String imageName : trmodel.getImageNames()) {
                TypeRoomImage typeRoomImage = new TypeRoomImage();
                typeRoomImage.setImageName(imageName);
                typeRoomImage.setTypeRoom(savedTypeRoom);
                typeRoomImageRepository.save(typeRoomImage);
            }
        }

        // Chuyển đổi và trả về DTO
        return convertTypeRoomDto(savedTypeRoom);
    }

    // cập nhật dịch vụ phòng
    public TypeRoomDto updateTypeRoom( typeRoomModel trModel) {
        List<String> errorMessages = new ArrayList<>(); // Danh sách lưu trữ các thông báo lỗi

        // Kiểm tra xem loại phòng có tồn tại hay không
        Optional<TypeRoom> existingTypeRoomOpt = typeRoomRepository.findById(trModel.getId());
        if (!existingTypeRoomOpt.isPresent()) {
            throw new EntityNotFoundException("Loại phòng với ID " + trModel.getId() + " không tồn tại.");
        }

        TypeRoom existingTypeRoom = existingTypeRoomOpt.get();
            // Cập nhật các thuộc tính cho loại phòng
            existingTypeRoom.setTypeRoomName(trModel.getTypeRoomName());
            existingTypeRoom.setPrice(trModel.getPrice());
            existingTypeRoom.setBedCount(trModel.getBedCount());
            existingTypeRoom.setAcreage(trModel.getAcreage());
            Optional<TypeBed> typeBed = typeBedRepository.findById(trModel.getTypeBedId());
            existingTypeRoom.setTypeBed(typeBed.get());
            existingTypeRoom.setGuestLimit(trModel.getGuestLimit());
            existingTypeRoom.setDescribes(trModel.getDescribes());
            List<TypeRoomImage> typeRoomImages = new ArrayList<>();
            existingTypeRoom.setTypeRoomImages(typeRoomImages);

        // Lưu loại phòng đã cập nhật vào cơ sở dữ liệu và chuyển đổi sang DTO
        TypeRoom updatedTypeRoom = typeRoomRepository.save(existingTypeRoom);

        //Xóa tất cả các tiện nghi cũ
        List<TypeRoomAmenitiesTypeRoom> amenitiesTypeRooms = updatedTypeRoom.getTypeRoomAmenitiesTypeRoomList();
        for (TypeRoomAmenitiesTypeRoom item : amenitiesTypeRooms) {
            typeRoomAmenitiesTypeRoomRepository.delete(item);
        }

        //Cập nhật thông tin tiện nghi loại phòng
        List<amenitiesTypeRoomModel> amenitiesTypeRoomModel = trModel.getAmenitiesTypeRooms();
        for (amenitiesTypeRoomModel item : amenitiesTypeRoomModel) {
            TypeRoomAmenitiesTypeRoom typeRoomAmenitiesTypeRoom = new TypeRoomAmenitiesTypeRoom();
            typeRoomAmenitiesTypeRoom.setTypeRoom(updatedTypeRoom);
            Optional<AmenitiesTypeRoom> amenitiesTypeRoom = amenitiesTypeRoomRepository.findById(item.getId());
            typeRoomAmenitiesTypeRoom.setAmenitiesTypeRoom(amenitiesTypeRoom.get());
            typeRoomAmenitiesTypeRoomRepository.save(typeRoomAmenitiesTypeRoom);
        }
        return convertTypeRoomDto(updatedTypeRoom); // Chuyển đổi loại phòng đã lưu sang DTO
    }

    // xóa dịch vụ phòng
    @Transactional
    public void deleteTypeRoom(Integer id) {
        if (!typeRoomRepository.existsById(id)) {
            throw new NoSuchElementException("Loại phòng này không tồn tại"); // Ném ngoại lệ nếu không tồn tại
        }

        Optional<TypeRoom> optionalTypeRoomOpt = typeRoomRepository.findById(id);
        if (optionalTypeRoomOpt.isPresent()) {
            TypeRoom typeRoom = optionalTypeRoomOpt.get();
            List<TypeRoomImage> typeRoomImages = typeRoom.getTypeRoomImages();

            // Xóa ảnh trước khi xóa loại phòng
            for (TypeRoomImage typeRoomImage : typeRoomImages) {
                typeRoomImageRepository.delete(typeRoomImage);
            }
            List<TypeRoomAmenitiesTypeRoom> typeRoomAmenitiesTypeRoom = typeRoom.getTypeRoomAmenitiesTypeRoomList();
            for (TypeRoomAmenitiesTypeRoom item : typeRoomAmenitiesTypeRoom) {
                typeRoomAmenitiesTypeRoomRepository.delete(item);
            }
            // Sau khi xóa ảnh thành công, xóa loại phòng
            typeRoomRepository.delete(typeRoom);
        }
    }


    public List<TypeRoomDto> getTypeRooms() {
        List<TypeRoom> list = typeRoomRepository.findTop3TypeRooms();
        return list.stream().map(this::convertTypeRoomDto).toList();
    }

    public List<TypeRoomAmenitiesTypeRoomModel> getTypeRoomAmenitiesTypeRoom(Integer idTypeRoom) {
        List<TypeRoomAmenitiesTypeRoom> list = typeRoomAmenitiesTypeRoomRepository.findByTypeRoomId(idTypeRoom);
        List<TypeRoomAmenitiesTypeRoomModel> typeRoomAmenitiesTypeRoomModels = new ArrayList<>();
        for (TypeRoomAmenitiesTypeRoom item : list) {
            TypeRoomAmenitiesTypeRoomModel typeRoomAmenitiesTypeRoomModel = new TypeRoomAmenitiesTypeRoomModel();
            typeRoomAmenitiesTypeRoomModel.setValue(String.valueOf(item.getAmenitiesTypeRoom().getId()));
            typeRoomAmenitiesTypeRoomModel.setLabel(item.getAmenitiesTypeRoom().getAmenitiesTypeRoomName());
            typeRoomAmenitiesTypeRoomModels.add(typeRoomAmenitiesTypeRoomModel);
        }
        return typeRoomAmenitiesTypeRoomModels;
    }

    public TypeRoomDto getTypeRoomsById(Integer id) {
        Optional<TypeRoom> optional = typeRoomRepository.findById(id);
        if (optional.isPresent()) {
            TypeRoom typeRoom = optional.get();
            // Chuyển đổi TypeRoom thành TypeRoomDto
            return convertTypeRoomDto(typeRoom);
        } else {
            // Trả về null hoặc có thể ném exception nếu phòng không tồn tại
            throw new RuntimeException("TypeRoom not found for id: " + id);
        }
    }

}
