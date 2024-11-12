package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.*;
import com.hotel.hotel_stars.Entity.Booking;
import com.hotel.hotel_stars.Entity.BookingRoom;
import com.hotel.hotel_stars.Repository.BookingRoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.geom.QuadCurve2D;
import java.util.List;

@Service
public class BookingRoomService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BookingRoomRepository bookingRoomRepository;

    public BookingRoomDto toDTO(BookingRoom bookingRoom) {
        BookingRoomDto bookingRoomDto = modelMapper.map(bookingRoom, BookingRoomDto.class);

        RoleDto roleDto = new RoleDto();
        roleDto.setId(bookingRoom.getBooking().getAccount().getRole().getId());
        roleDto.setRoleName(bookingRoom.getBooking().getAccount().getRole().getRoleName());

        AccountDto accountDto = new AccountDto();
        accountDto.setId(bookingRoom.getBooking().getAccount().getId());
        accountDto.setUsername(bookingRoom.getBooking().getAccount().getUsername());
        accountDto.setFullname(bookingRoom.getBooking().getAccount().getFullname());
        accountDto.setPhone(bookingRoom.getBooking().getAccount().getPhone());
        accountDto.setEmail(bookingRoom.getBooking().getAccount().getEmail());
        accountDto.setAvatar(bookingRoom.getBooking().getAccount().getAvatar());
        accountDto.setGender(bookingRoom.getBooking().getAccount().getGender());
        accountDto.setIsDelete(bookingRoom.getBooking().getAccount().getIsDelete());
        accountDto.setRoleDto(roleDto);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(bookingRoom.getBooking().getId());
        bookingDto.setCreateAt(bookingRoom.getBooking().getCreateAt());
        bookingDto.setStartAt(bookingRoom.getBooking().getStartAt());
        bookingDto.setEndAt(bookingRoom.getBooking().getEndAt());
        bookingDto.setStatusPayment(bookingRoom.getBooking().getStatusPayment());
        bookingDto.setAccountDto(accountDto);

        FloorDto floorDto = new FloorDto();
        floorDto.setId(bookingRoom.getRoom().getFloor().getId());
        floorDto.setFloorName(bookingRoom.getRoom().getFloor().getFloorName());

        TypeBedDto typeBedDto = new TypeBedDto();
        typeBedDto.setId(bookingRoom.getRoom().getTypeRoom().getId());
        typeBedDto.setBedName(bookingRoom.getRoom().getTypeRoom().getTypeRoomName());

        TypeRoomDto typeRoomDto = new TypeRoomDto();
        typeRoomDto.setId(bookingRoom.getBooking().getTypeRoom().getId());
        typeRoomDto.setTypeRoomName(bookingRoom.getBooking().getTypeRoom().getTypeRoomName());
        typeRoomDto.setPrice(bookingRoom.getBooking().getTypeRoom().getPrice());
        typeRoomDto.setBedCount(bookingRoom.getBooking().getTypeRoom().getBedCount());
        typeRoomDto.setAcreage(bookingRoom.getBooking().getTypeRoom().getAcreage());
        typeRoomDto.setGuestLimit(bookingRoom.getBooking().getTypeRoom().getGuestLimit());
        typeRoomDto.setDescribes(bookingRoom.getBooking().getTypeRoom().getDescribes());
        typeRoomDto.setTypeBedDto(typeBedDto);

        StatusRoomDto statusRoomDto = new StatusRoomDto();
        statusRoomDto.setId(bookingRoom.getRoom().getStatusRoom().getId());
        statusRoomDto.setStatusRoomName(bookingRoom.getRoom().getStatusRoom().getStatusRoomName());

        RoomDto roomDto = new RoomDto();
        roomDto.setId(bookingRoom.getRoom().getId());
        roomDto.setRoomName(bookingRoom.getRoom().getRoomName());
        roomDto.setFloorDto(floorDto);
        roomDto.setTypeRoomDto(typeRoomDto);
        roomDto.setStatusRoomDto(statusRoomDto);

        // Ánh xạ các đối tượng đầy đủ của Booking và Room
        bookingRoomDto.setBooking(bookingDto);
        bookingRoomDto.setRoom(roomDto);

        return bookingRoomDto;
    }

    public List<BookingRoomDto> getAllBookingRooms() {
        List<BookingRoom> list = bookingRoomRepository.findAll();
        return list.stream().map(this::toDTO).toList();
    }
}
