package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.*;
import com.hotel.hotel_stars.DTO.Select.RoomTypeDetail;
import com.hotel.hotel_stars.DTO.selectDTO.FindTypeRoomDto;
import com.hotel.hotel_stars.Entity.*;
import com.hotel.hotel_stars.Models.typeRoomModel;
import com.hotel.hotel_stars.Repository.*;
import com.hotel.hotel_stars.Repository.TypeBedRepository;
import com.hotel.hotel_stars.Repository.TypeRoomImageRepository;
import com.hotel.hotel_stars.Repository.TypeRoomRepository;
import com.hotel.hotel_stars.utils.paramService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TypeRoomService {
    @Autowired
    TypeRoomRepository typeRoomRepository;

    @Autowired
    TypeBedRepository typeBedRepository;
    @Autowired
    paramService paramServices;

    @Autowired
    TypeRoomImageRepository typeRoomImageRepository;

    @Autowired
    AmenitiesTypeRoomRepository amenitiesTypeRoomRepository;

    @Autowired
    TypeRoomAmenitiesTypeRoomRepository typeRoomAmenitiesTypeRoomRepository;

    @Autowired
    FeedBackRepository feedBackRepository;
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
                tr.getAcreage(), tr.getGuestLimit(), typeBedDto, tr.getDescribes(), typeRoomImageDtos);
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
    public TypeRoomDto updateTypeRoom(typeRoomModel trModel) {
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


    public List<TypeRoomWithReviewsDTO> getTypeRooms() {
        List<Object[]> result = typeRoomRepository.findTop3TypeRoomsWithGoodReviews();
        List<TypeRoomWithReviewsDTO> dtos = new ArrayList<>();

        result.forEach(row -> {
            Integer id = (Integer) row[0];
            String typeRoomName = (String) row[1];

            // Sử dụng BigDecimal cho giá trị price và acreage thay vì Double
            Double price = (Double) row[2];
            Integer bedCount = (Integer) row[3];
            Double acreage = (Double) row[4];
            Integer guestLimit = (Integer) row[5];
            String describes = (String) row[6];

            Integer imageId = (Integer) row[7];

            // Lấy danh sách hình ảnh và chuyển đổi thành DTO
            List<TypeRoomImage> typeRoomImage = typeRoomImageRepository.findByTypeRoomId(id);
            List<TypeRoomImageDto> typeRoomImageDtos = new ArrayList<>();
            typeRoomImage.forEach(typeImage -> {
                TypeRoomImageDto typeRoomDto = new TypeRoomImageDto();
                typeRoomDto.setId(typeImage.getId());
                typeRoomDto.setImageName(typeImage.getImageName());
                typeRoomImageDtos.add(typeRoomDto);
            });

            // Lấy danh sách tiện nghi và chuyển đổi thành DTO
            List<TypeRoomAmenitiesTypeRoom> amenitiesTypeRoom = typeRoomAmenitiesTypeRoomRepository.findByTypeRoom_Id(id);
            List<TypeRoomAmenitiesTypeRoomDto> amenitiesDtos = new ArrayList<>();
            amenitiesTypeRoom.forEach(amenities -> {
                AmenitiesTypeRoom amenitiesTypeRoomDto = amenitiesTypeRoomRepository.findById(amenities.getAmenitiesTypeRoom().getId()).get();
                AmenitiesTypeRoomDto roomDto = new AmenitiesTypeRoomDto();
                roomDto.setId(amenitiesTypeRoomDto.getId());
                roomDto.setAmenitiesTypeRoomName(amenitiesTypeRoomDto.getAmenitiesTypeRoomName());

                TypeRoomAmenitiesTypeRoomDto typeRoomAmenitiesTypeRoomDto = new TypeRoomAmenitiesTypeRoomDto();
                typeRoomAmenitiesTypeRoomDto.setId(amenities.getId());
                typeRoomAmenitiesTypeRoomDto.setAmenitiesTypeRoomDto(roomDto);

                amenitiesDtos.add(typeRoomAmenitiesTypeRoomDto);
            });

            Long totalReviews = (Long) row[9];
            BigDecimal averageStarsConvert = (BigDecimal) row[10];
            Double averageStars = averageStarsConvert.doubleValue();

            // Tạo DTO cho TypeRoomWithReviewsDTO
            TypeRoomWithReviewsDTO typeRoomWithReviewsDTO = new TypeRoomWithReviewsDTO(
                    id,
                    typeRoomName,
                    price,         // Trả về BigDecimal
                    bedCount,
                    acreage,       // Trả về BigDecimal
                    guestLimit,
                    describes,
                    typeRoomImageDtos,
                    amenitiesDtos,
                    totalReviews,
                    averageStars
            );
            dtos.add(typeRoomWithReviewsDTO);
        });

        return dtos;
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

    public List<FindTypeRoomDto> getRoom(String startDates, String endDates, Integer guestLimit) {
        List<FindTypeRoomDto> dtoList =new ArrayList<>();
        Instant starDate = paramServices.stringToInstant(startDates);
        Instant endDate = paramServices.stringToInstant(endDates);
        List<Object[]> result = typeRoomRepository.findAvailableRooms(starDate,endDate,guestLimit);
        for (Object[] results : result) {
            // Assuming the results array contains data in the correct order:
            // Adjust the indices to match the actual data order returned by your query.
            Integer roomId = (Integer) results[0];
            String roomName = (String) results[1];
            Integer roomTypeId = (Integer) results[2];
            String roomTypeName = (String) results[3];
            Double priceTypeRoom = (Double) results[4];
            Double acreage = (Double) results[5];
            Integer guestLimits = (Integer) results[6];
            String amenitiesTypeRoomDetails = (String) results[7];
            Double estCost = (Double) results[8];
            String imagesString = (String) results[9];
            List<String> listImages = Arrays.stream(imagesString.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
            String describe=(String) results[10];
            // Create a new DTO object and add it to the list
            FindTypeRoomDto dto = new FindTypeRoomDto(
                    roomId,
                    roomName,
                    roomTypeId,
                    roomTypeName,
                    priceTypeRoom,
                    acreage,
                    guestLimits,
                    amenitiesTypeRoomDetails,
                    estCost,
                    listImages,
                    describe
            );
            dtoList.add(dto);
        }
        return dtoList;
    }

    public List<RoomTypeDetail> getRoomTypeDetailById(Integer roomId) {
        List<Object[]> results = typeRoomRepository.findTypeRoomDetailsById(roomId); // Adjust the method call if needed
        List<RoomTypeDetail> dtos = new ArrayList<>();

        results.forEach(row -> {
            Integer typeRoomId = (Integer) row[0];
            String typeRoomName = (String) row[1];
            Double price = (Double) row[2];           // Changed from Integer to Double
            Integer bedCount = (Integer) row[3];
            Double acreage = (Double) row[4];
            Integer guestLimit = (Integer) row[5];
            String describes = (String) row[6];
            String bedName = (String) row[7];

            // Split imageList (comma-separated string of image URLs) into a List of Strings
            List<String> imageList = new ArrayList<>();
            if (row[8] != null) {
                imageList = Arrays.asList(((String) row[8]).split(","));
            }

            // Split amenitiesList (comma-separated string of IDs) into a List of Integers
            List<Integer> amenitiesList = new ArrayList<>();
            if (row[9] != null) {
                amenitiesList = Arrays.stream(((String) row[9]).split(","))
                        .map(Integer::parseInt)  // Convert each string to Integer
                        .toList();
            }
            List<AmenitiesTypeRoom> amenitiesTypeRooms = amenitiesTypeRoomRepository.findAllById(amenitiesList);
            List<AmenitiesTypeRoomDto> amenitiesTypeRoomDtos = amenitiesTypeRooms.stream()
                    .map(amenitiesTypeRoom -> new AmenitiesTypeRoomDto(
                            amenitiesTypeRoom.getId(),  // Hoặc các trường khác cần thiết
                            amenitiesTypeRoom.getAmenitiesTypeRoomName() // Chuyển các trường khác nếu cần
                    ))
                    .toList();

            List<Integer> feedBack = new ArrayList<>();
            if (row[10] != null) {
                feedBack = Arrays.stream(((String) row[10]).split(","))
                        .map(Integer::parseInt)
                        .toList();
            }

            List<Feedback> feedbacks = feedBackRepository.findAllById(feedBack);
            List<FeedbackDto> feedbackDtos = feedbacks.stream().map(feedback -> new FeedbackDto(
                    feedback.getId(),
                    feedback.getContent(),
                    feedback.getStars(),
                    feedback.getCreateAt(),
                    feedback.getRatingStatus(),
                    null
            )).toList();

            BigDecimal averageFeedBackBigDecimal = (BigDecimal) row[11];
            Double averageFeedBack = averageFeedBackBigDecimal.doubleValue();
            String accountName = (String) row[12];
            String imageName = (String) row[13];
            // Create and populate RoomTypeDetail object
            RoomTypeDetail detail = new RoomTypeDetail();
            detail.setTypeRoomId(typeRoomId);
            detail.setTypeRoomName(typeRoomName);
            detail.setPrice(price);
            detail.setBedCount(bedCount);
            detail.setAcreage(acreage);
            detail.setGuestLimit(guestLimit);
            detail.setDescribes(describes);
            detail.setBedName(bedName);
            detail.setImageList(imageList);
            detail.setAmenitiesList(amenitiesTypeRoomDtos);
            detail.setFeedBack(feedbackDtos);
            detail.setAverageFeedBack(averageFeedBack);
            detail.setAccountName(accountName);
            detail.setImage(imageName);
            dtos.add(detail);
        });

        return dtos;
    }
}
