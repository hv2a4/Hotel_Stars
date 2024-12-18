package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.*;
import com.hotel.hotel_stars.Entity.Booking;
import com.hotel.hotel_stars.Entity.BookingRoom;
import com.hotel.hotel_stars.Entity.BookingRoomServiceRoom;
import com.hotel.hotel_stars.Entity.StatusBooking;
import com.hotel.hotel_stars.Entity.TypeRoom;
import com.hotel.hotel_stars.Entity.TypeRoomImage;
import com.hotel.hotel_stars.Repository.BookingRoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingRoomService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BookingRoomRepository bookingRoomRepository;

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

	public BookingRoomDto toDTO(BookingRoom bookingRoom) {
		BookingRoomDto bookingRoomDto = modelMapper.map(bookingRoom, BookingRoomDto.class);

		RoleDto roleDto = new RoleDto();
		roleDto.setId(bookingRoom.getBooking().getAccount().getRole().getId());
		roleDto.setRoleName(bookingRoom.getBooking().getAccount().getRole().getRoleName());

		FloorDto floorDto = new FloorDto();
		floorDto.setId(bookingRoom.getRoom().getFloor().getId());
		floorDto.setFloorName(bookingRoom.getRoom().getFloor().getFloorName());

		TypeBedDto typeBedDto = new TypeBedDto();
		typeBedDto.setId(bookingRoom.getRoom().getTypeRoom().getId());
		typeBedDto.setBedName(bookingRoom.getRoom().getTypeRoom().getTypeRoomName());

		AccountDto accountDto = null;
		if (bookingRoom.getAccount() != null) {
			accountDto = new AccountDto();
			accountDto.setId(bookingRoom.getAccount().getId());
			accountDto.setUsername(bookingRoom.getAccount().getUsername());
			accountDto.setFullname(bookingRoom.getAccount().getFullname());
			accountDto.setPhone(bookingRoom.getAccount().getPhone());
			accountDto.setEmail(bookingRoom.getAccount().getEmail());
			accountDto.setAvatar(bookingRoom.getAccount().getAvatar());
			accountDto.setGender(bookingRoom.getAccount().getGender());
			accountDto.setIsDelete(bookingRoom.getAccount().getIsDelete());
			accountDto.setRoleDto(roleDto);
		}

		BookingDto bookingDto = new BookingDto();
		bookingDto.setId(bookingRoom.getBooking().getId());
		bookingDto.setCreateAt(bookingRoom.getBooking().getCreateAt());
		bookingDto.setStartAt(bookingRoom.getBooking().getStartAt());
		bookingDto.setEndAt(bookingRoom.getBooking().getEndAt());
		bookingDto.setDescriptions(bookingRoom.getBooking().getDescriptions());
		bookingDto.setStatusPayment(bookingRoom.getBooking().getStatusPayment());
		bookingDto.setDiscountPercent(bookingRoom.getBooking().getDiscountPercent());
		StatusBookingDto statusBookingDto = new StatusBookingDto();
		statusBookingDto.setStatusBookingName(bookingRoom.getBooking().getStatus().getStatusBookingName());
		statusBookingDto.setId(bookingRoom.getBooking().getStatus().getId());
		bookingDto.setStatusDto(statusBookingDto);
		AccountDto accountBookingDto = new AccountDto();
		accountBookingDto.setId(bookingRoom.getBooking().getAccount().getId());
		accountBookingDto.setUsername(bookingRoom.getBooking().getAccount().getUsername());
		accountBookingDto.setFullname(bookingRoom.getBooking().getAccount().getFullname());
		accountBookingDto.setPhone(bookingRoom.getBooking().getAccount().getPhone());
		accountBookingDto.setEmail(bookingRoom.getBooking().getAccount().getEmail());
		accountBookingDto.setAvatar(bookingRoom.getBooking().getAccount().getAvatar());
		accountBookingDto.setGender(bookingRoom.getBooking().getAccount().getGender());
		accountBookingDto.setIsDelete(bookingRoom.getBooking().getAccount().getIsDelete());
		bookingDto.setAccountDto(accountBookingDto);
		StatusRoomDto statusRoomDto = new StatusRoomDto();
		statusRoomDto.setId(bookingRoom.getRoom().getStatusRoom().getId());
		statusRoomDto.setStatusRoomName(bookingRoom.getRoom().getStatusRoom().getStatusRoomName());

		RoomDto roomDto = new RoomDto();
		roomDto.setId(bookingRoom.getRoom().getId());
		roomDto.setRoomName(bookingRoom.getRoom().getRoomName());
		roomDto.setFloorDto(floorDto);
		roomDto.setStatusRoomDto(statusRoomDto);
		roomDto.setTypeRoomDto(convertTypeRoomDto(bookingRoom.getRoom().getTypeRoom()));
		// Ánh xạ các đối tượng đầy đủ của Booking và Room
		bookingRoomDto.setBooking(bookingDto);
		bookingRoomDto.setAccountDto(accountDto);
		bookingRoomDto.setRoom(roomDto);

		return bookingRoomDto;
	}

    public List<BookingRoomDto> convertListDto(List<BookingRoom> bookingRoom){
    	return bookingRoom.stream().map(this::toDTO).toList();
    }
    public List<BookingRoomDto> getAllBookingRooms() {
        List<BookingRoom> list = bookingRoomRepository.findAll();
        return list.stream().map(this::toDTO).toList();
    }
    public List<BookingRoomDto> getBookingRoomAccount(Integer id) {
    	List<BookingRoom> list = bookingRoomRepository.findBookingRoomByAccountId(id);
    	return list.stream().map(this::toDTO).toList();
    }
    public List<BookingRoomDto> getByRoom(Integer roomId, Integer statusId){
    	List<BookingRoom> bookingRoom = bookingRoomRepository.findByRoom_IdAndRoom_StatusRoom_Id(roomId, statusId);
    	return bookingRoom.stream().map(this::toDTO).toList();
    }
    public List<BookingRoomDto> getBookingRoomIds(List<Integer> ids){
    	List<BookingRoom> bookingRooms = bookingRoomRepository.findByIdIn(ids);
    	return bookingRooms.stream().map(this::toDTO).toList();
    }
    //khôi
    public BookingRoomDto getByIdBookingRoom(Integer id) {
    	BookingRoom bookingRoom = bookingRoomRepository.findById(id).get();
    	return toDTO(bookingRoom);
    }
    
    public BookingRoomDto getBookingRoomByRoom(Integer idRoom) {
        BookingRoom bookingRoom = bookingRoomRepository.findFirstBookingRoomByRoomIdAndStatusNotIn(idRoom);
        if (bookingRoom == null) {
            return null; // Trả về null nếu không tìm thấy
        }
        return toDTO(bookingRoom); // Chuyển đổi đối tượng sang DTO nếu tìm thấy
    }

    //khôi
}
