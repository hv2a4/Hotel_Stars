package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.TypeBedDto;
import com.hotel.hotel_stars.DTO.TypeRoomDto;
import com.hotel.hotel_stars.DTO.TypeRoomImageDto;
import com.hotel.hotel_stars.DTO.selectDTO.FindTypeRoomDto;
import com.hotel.hotel_stars.Entity.TypeBed;
import com.hotel.hotel_stars.Entity.TypeRoom;
import com.hotel.hotel_stars.Entity.TypeRoomImage;
import com.hotel.hotel_stars.Models.typeRoomModel;
import com.hotel.hotel_stars.Repository.TypeBedRepository;
import com.hotel.hotel_stars.Repository.TypeRoomImageRepository;
import com.hotel.hotel_stars.Repository.TypeRoomRepository;
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
        List<String> errorMessages = new ArrayList<>(); // Danh sách lưu trữ các thông báo lỗi

            TypeRoom typeRoom = new TypeRoom();

            // Đặt thông tin loại phòng
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

//             Lưu hình ảnh vào bảng TypeRoomImage

            if (trmodel.getImageNames() != null) {
                for (String imageName : trmodel.getImageNames()) {
                    TypeRoomImage typeRoomImage = new TypeRoomImage();
                    typeRoomImage.setImageName(imageName);
                    typeRoomImage.setTypeRoom(savedTypeRoom); // Gán phòng vào hình ảnh
                    typeRoomImageRepository.save(typeRoomImage); // Lưu hình ảnh
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

            // Sau khi xóa ảnh thành công, xóa loại phòng
            typeRoomRepository.delete(typeRoom);
        }
    }


    public List<TypeRoomDto> getTypeRooms() {
        List<TypeRoom> list = typeRoomRepository.findTop3TypeRooms();
        return list.stream().map(this::convertTypeRoomDto).toList();
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
