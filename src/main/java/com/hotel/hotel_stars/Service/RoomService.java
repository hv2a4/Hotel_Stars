package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.*;
import com.hotel.hotel_stars.DTO.Select.*;
import com.hotel.hotel_stars.DTO.selectDTO.countDto;
import com.hotel.hotel_stars.Entity.*;
import com.hotel.hotel_stars.Models.RoomModel;
import com.hotel.hotel_stars.Repository.RoomRepository;
import com.hotel.hotel_stars.Repository.TypeRoomImageRepository;
import com.hotel.hotel_stars.Utils.paramService;
import com.hotel.hotel_stars.Repository.StatusRoomRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.stream.Collectors;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    paramService paramServices;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    StatusRoomRepository statusRoomRepository;

    @Autowired
    TypeRoomImageRepository typeRoomImageRepository;

    public RoomDto convertToDto(Room room) {

        FloorDto floorDto = new FloorDto();
        floorDto.setId(room.getFloor().getId());
        floorDto.setFloorName(room.getFloor().getFloorName());

        StatusRoomDto statusRoomDto = new StatusRoomDto();
        statusRoomDto.setId(room.getStatusRoom().getId());
        statusRoomDto.setStatusRoomName(room.getStatusRoom().getStatusRoomName());

        TypeBedDto typeBedDto = new TypeBedDto();
        typeBedDto.setId(room.getTypeRoom().getTypeBed().getId());
        typeBedDto.setBedName(room.getTypeRoom().getTypeBed().getBedName());

        TypeRoomDto typeRoom = new TypeRoomDto();
        typeRoom.setId(room.getTypeRoom().getId());
        typeRoom.setTypeRoomName(room.getTypeRoom().getTypeRoomName());
        typeRoom.setPrice(room.getTypeRoom().getPrice());
        typeRoom.setBedCount(room.getTypeRoom().getBedCount());
        typeRoom.setAcreage(room.getTypeRoom().getAcreage());
        typeRoom.setGuestLimit(room.getTypeRoom().getGuestLimit());
        typeRoom.setTypeBedDto(typeBedDto);
        typeRoom.setDescribes(room.getTypeRoom().getDescribes());

        RoomDto roomDto = new RoomDto();
        roomDto.setId(room.getId());
        roomDto.setRoomName(room.getRoomName());
        roomDto.setFloorDto(floorDto);
        roomDto.setTypeRoomDto(typeRoom);
        roomDto.setStatusRoomDto(statusRoomDto);
        return roomDto;
    }

    public List<RoomDto> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream().map(this::convertToDto).toList();
    }

    public StatusResponseDto PostRoom(RoomModel roomModel) {
        try {
            // Kiểm tra trùng tên phòng
            if (roomRepository.existsByRoomName(roomModel.getRoomName())) {
                return new StatusResponseDto("409", "Conflict", "Tên phòng đã tồn tại");
            }

            // Tạo đối tượng Room từ RoomModel
            Room room = new Room();
            TypeRoom roomType = new TypeRoom();
            roomType.setId(roomModel.getTypeRoomId());
            StatusRoom statusRoom = new StatusRoom();
            statusRoom.setId(roomModel.getStatusRoomId());
            Floor floor = new Floor();
            floor.setId(roomModel.getFloorId());
            room.setRoomName(roomModel.getRoomName());
            room.setTypeRoom(roomType);
            room.setStatusRoom(statusRoom);
            room.setFloor(floor);

            // Lưu phòng vào cơ sở dữ liệu
            roomRepository.save(room);

            return new StatusResponseDto("200", "Success", "Phòng đã được thêm thành công");
        } catch (RuntimeException e) {
            return new StatusResponseDto("400", "Bad Request", e.getMessage());
        } catch (Exception e) {
            return new StatusResponseDto("500", "Error", "Có lỗi xảy ra khi thêm phòng: " + e.getMessage());
        }
    }

    public RoomDto getById(Integer id) {
        Room room = roomRepository.findById(id).get();
        return convertToDto(room);
    }

    public countDto displayCounts() {
        List<Object[]> results = roomRepository.getCounts();
        countDto dto = new countDto();
        for (Object[] result : results) {
            // Chuyển đổi đúng kiểu
            dto.setCountStaff(((Number) result[0]).longValue());
            dto.setCountCustomers(((Number) result[1]).longValue());
            dto.setTotalRoom(((Number) result[2]).longValue());

        }
        return dto;
    }

    public StatusResponseDto PutRoom(RoomModel roomModel) {
        try {
            Room room = roomRepository.findById(roomModel.getId())
                    .orElseThrow(() -> new RuntimeException("Phòng không tồn tại")); // Handle room not found

            // Validate room data
            // Validate: Check if the room name exists in other rooms
            if (roomRepository.existsByRoomNameAndIdNot(roomModel.getRoomName(), roomModel.getId())) {
                return new StatusResponseDto("409", "Conflict", "Tên phòng đã tồn tại ở một phòng khác");
            }
            // Add more validations as needed

            TypeRoom roomType = new TypeRoom();
            roomType.setId(roomModel.getTypeRoomId());
            StatusRoom statusRoom = new StatusRoom();
            statusRoom.setId(roomModel.getStatusRoomId());
            Floor floor = new Floor();
            floor.setId(roomModel.getFloorId());
            room.setRoomName(roomModel.getRoomName());
            room.setTypeRoom(roomType);
            room.setStatusRoom(statusRoom);
            room.setFloor(floor);

            roomRepository.save(room); // Save the updated room

            return new StatusResponseDto("200", "Success", "Phòng đã được cập nhật thành công");
        } catch (RuntimeException e) {
            return new StatusResponseDto("400", "Bad Request", e.getMessage());
        } catch (Exception e) {
            // Log the exception if necessary
            return new StatusResponseDto("500", "Error", "Có lỗi xảy ra khi cập nhật phòng: " + e.getMessage());
        }
    }

    public StatusResponseDto deleteById(Integer id) {
        StatusResponseDto statusResponseDto = new StatusResponseDto();
        try {
            // Kiểm tra xem phòng có tồn tại trước khi xóa
            if (!roomRepository.existsById(id)) {
                statusResponseDto.setCode("404");
                statusResponseDto.setStatus("Not Found");
                statusResponseDto.setMessage("Không tìm thấy phòng với ID: " + id);
                return statusResponseDto;
            }

            roomRepository.deleteById(id);
            statusResponseDto.setCode("200");
            statusResponseDto.setStatus("Success");
            statusResponseDto.setMessage("Xóa thành công phòng với ID: " + id);
        } catch (Exception e) {
            statusResponseDto.setCode("500");
            statusResponseDto.setStatus("Error");
            statusResponseDto.setMessage("Xóa thất bại: " + e.getMessage());
        }
        return statusResponseDto;
    }

    public List<RoomDto> getByFloorId(Integer id) {
        List<Room> rooms = roomRepository.findByFloorId(id);
        return rooms.stream().map(this::convertToDto).toList();
    }

    public java.sql.Date stringToSqlDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return java.sql.Date.valueOf(localDate); // Trả về java.sql.Date
    }

    public LocalDate stringToLocalDate(String dateStr) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
    }

    public PaginatedResponseDto<RoomDto> getAll(int page, int size, String sortBy, LocalDate startDate, LocalDate endDate,
                                                Integer guestLimit) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

// Chuyển đổi LocalDate thành Date hoặc Instant khi truyền vào JPA Query
        Instant startInstant = startDate != null ? startDate.atStartOfDay(ZoneId.systemDefault()).toInstant() : null;
        Instant endInstant = endDate != null ? endDate.atStartOfDay(ZoneId.systemDefault()).toInstant() : null;

        Page<Room> roomPage = roomRepository.findAvailableRoomsWithPagination(
                startInstant, endInstant, guestLimit, pageable
        );

        List<RoomDto> roomDtos = roomPage.stream().map(this::convertToDto).collect(Collectors.toList());
        return new PaginatedResponseDto<>(roomDtos, page, roomPage.getTotalPages(), roomPage.getTotalElements());
    }

    // public PaginatedResponseDto<RoomDto> getAll(int page, int size, String
    // sortBy, Instant startDate, Instant endDate, Integer guestLimit) {
    // int offset = (page - 1) * size;
    //
    // List<Object[]> roomData =
    // roomRepository.findAvailableRoomsWithPagination(startDate, endDate,
    // guestLimit, size, offset);
    //
    // List<RoomDto> roomDtos = roomData.stream().map(this::convertToDto).toList();
    //
    // long totalElements = roomRepository.countAvailableRooms(startDate, endDate,
    // guestLimit); // Thêm phương thức đếm tổng số phòng
    //
    // return new PaginatedResponseDto<>(
    // roomDtos,
    // page,
    // (int) Math.ceil((double) totalElements / size),
    // totalElements
    // );
    // }

    public StatusResponseDto updateActiveRoom(RoomModel model) {
        try {
            Room room = roomRepository.findById(model.getId())
                    .orElseThrow(() -> new RuntimeException("Phòng không tồn tại"));
            StatusRoom status = statusRoomRepository.findById(model.getStatusRoomId()).get();
            room.setStatusRoom(status);
            roomRepository.save(room);
            return new StatusResponseDto("200", "success", "Phòng đã được cập nhật thành công");
        } catch (RuntimeException e) {
            return new StatusResponseDto("400", "error", "Lỗi không xác định");
        } catch (Exception e) {
            // Log the exception if necessary
            return new StatusResponseDto("500", "error", "Có lỗi xảy ra khi cập nhật trạng thái phòng");
        }

    }

    public Page<RoomAvailabilityInfo> getAvailableRooms(Pageable pageable) {
        Page<Object[]> result = roomRepository.findAvailableRooms(pageable);
        return result.map(row -> {
            String roomId = (String) row[0];
            System.out.println(roomId);
            List<Integer> IdRoom = Arrays.stream(String.valueOf(roomId).split(",")).map(String::trim).map(Integer::parseInt).toList();
            String roomName = (String) row[1];
            List<String> listRoom = Arrays.stream(String.valueOf(roomName).split(",")).map(String::trim).toList();
            Integer typeRoomId = (Integer) row[2];
            String typeRoomName = (String) row[3];
            Double price = (Double) row[4];
            Double acreage = (Double) row[5];
            Integer guestLimit = (Integer) row[6];
            String amenitiesDetails = (String) row[7];
            String imageList = (String) row[8];
            String description = (String) row[9];
            String bedNames = (String) row[10];
            String amenitiesIds = (String) row[11];
            Double finalPrice = (Double) row[12]; // finalPrice from SQL query
            Double estCost = (Double) row[13]; // estCost from SQL query

            RoomAvailabilityInfo roomAvailabilityInfo = new RoomAvailabilityInfo();
            roomAvailabilityInfo.setRoomId(IdRoom);
            roomAvailabilityInfo.setRoomName(listRoom);
            roomAvailabilityInfo.setTypeRoomId(typeRoomId);
            roomAvailabilityInfo.setTypeRoomName(typeRoomName);
            roomAvailabilityInfo.setPrice(price);
            roomAvailabilityInfo.setAcreage(acreage);
            roomAvailabilityInfo.setGuestLimit(guestLimit);
            roomAvailabilityInfo.setAmenitiesDetails(Arrays.stream(amenitiesDetails.split(",")).map(String::trim).toList());
            roomAvailabilityInfo.setImageList(Arrays.asList(imageList.split(",")));
            roomAvailabilityInfo.setDescription(description);
            roomAvailabilityInfo.setBedNames(Arrays.asList(bedNames.split(",")));
            roomAvailabilityInfo
                    .setAmenitiesIds(Arrays.stream(amenitiesIds.split(",")).map(Integer::parseInt).toList());

            // Setting the additional fields
            roomAvailabilityInfo.setFinalPrice(finalPrice);
            roomAvailabilityInfo.setEstCost(estCost);


            return roomAvailabilityInfo;
        });
    }

    public List<RoomDetailResponseDTO> getRoomDetailsByRoomId(Integer roomId) {
        List<Object[]> result = roomRepository.findRoomDetailsByRoomId(roomId);
        List<RoomDetailResponseDTO> roomDetailResponseDTOS = new ArrayList<>();

        result.forEach(row -> {
            Integer IdRoomId = (Integer) row[0];
            Integer typeRoomId = (Integer) row[1];
            String typeRoomName = (String) row[2];
            Double price = (Double) row[3];
            Integer bedCount = (Integer) row[4];
            Double acreage = (Double) row[5];
            Integer guestLimit = (Integer) row[6];
            String bedName = (String) row[7];
            String describes = (String) row[8];

            String servicesList = (String) row[9];
            List<String> imageIdStrings = List.of(servicesList.split(","));
            List<Integer> imageIds = imageIdStrings.stream().map(Integer::parseInt).toList();
            // Tìm tất cả các hình ảnh theo ID
            List<TypeRoomImage> typeRoomImages = typeRoomImageRepository.findAllById(imageIds);
            List<String> imageNames = new ArrayList<>();
            typeRoomImages.forEach(name -> {
                imageNames.add(name.getImageName());

            });

            String amenitiesIds = (String) row[10];
            List<String> amenitiesIdList = List.of(amenitiesIds.split(","));

            Double finalPrice = (Double) row[11];
            Double estCost = (Double) row[12];
            Double percent = (Double) row[13];

            RoomDetailResponseDTO roomDetailResponseDTO = new RoomDetailResponseDTO();
            roomDetailResponseDTO.setRoomId(IdRoomId);
            roomDetailResponseDTO.setTypeRoomId(typeRoomId);
            roomDetailResponseDTO.setTypeRoomName(typeRoomName);
            roomDetailResponseDTO.setPrice(price);
            roomDetailResponseDTO.setAcreage(acreage);
            roomDetailResponseDTO.setGuestLimit(guestLimit);
            roomDetailResponseDTO.setBedName(bedName);
            roomDetailResponseDTO.setDescribes(describes);
            roomDetailResponseDTO.setBedCount(bedCount);
            roomDetailResponseDTO.setImageNames(imageNames);
            roomDetailResponseDTO.setAmenities(amenitiesIdList);
            roomDetailResponseDTO.setFinalPrice(finalPrice);
            roomDetailResponseDTO.setEstCost(estCost);
            roomDetailResponseDTO.setPercent(percent);
            roomDetailResponseDTOS.add(roomDetailResponseDTO);
        });
        return roomDetailResponseDTOS;
    }

    public List<RoomListBooking> getRoomInBookingId(List<Integer> roomIdList) {
        List<Object[]> reusult = roomRepository.findRoomsDetailsByIds(roomIdList);
        List<RoomListBooking> roomListBookings = new ArrayList<>();
        reusult.forEach(row -> {

            Integer roomId = (Integer) row[0];
            String roomName = (String) row[1];
            Integer floorId = (Integer) row[2];
            Integer typeRoomId = (Integer) row[3];
            String typeRoomName = (String) row[4];
            Double price = (Double) row[5];
            Integer bedCount = (Integer) row[6];
            Double acreage = (Double) row[7];
            Integer guestLimit = (Integer) row[8];
            String describes = (String) row[9];
            String imageName = (String) row[10];

            List<String> imgName = Arrays.stream(imageName.split(",")).toList();

            RoomListBooking roomListBooking = new RoomListBooking();
            roomListBooking.setRoomId(roomId);
            roomListBooking.setRoomName(roomName);
            roomListBooking.setFloorId(floorId);
            roomListBooking.setTypeRoomId(typeRoomId);
            roomListBooking.setTypeRoomName(typeRoomName);
            roomListBooking.setPrice(price);
            roomListBooking.setAcreage(acreage);
            roomListBooking.setGuestLimit(guestLimit);
            roomListBooking.setDescribes(describes);
            roomListBooking.setBedCount(bedCount);
            roomListBooking.setAcreage(acreage);
            roomListBooking.setListImageName(imgName);
            roomListBookings.add(roomListBooking);
        });
        return roomListBookings;
    }

    public List<RoomListDTO> getListById(Integer id) {
        List<RoomListDTO> roomListDTOS = roomRepository.findRoomsByTypeId(id);
        return roomListDTOS;
    }
}
