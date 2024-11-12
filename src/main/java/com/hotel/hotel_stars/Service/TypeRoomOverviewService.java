package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.RoomDto;
import com.hotel.hotel_stars.DTO.Select.RoomInfoDTO;
import com.hotel.hotel_stars.DTO.Select.RoomReservationDTO;
import com.hotel.hotel_stars.DTO.Select.TypeRoomOverviewDTO;
import com.hotel.hotel_stars.DTO.TypeBedDto;
import com.hotel.hotel_stars.DTO.TypeRoomDto;
import com.hotel.hotel_stars.DTO.TypeRoomImageDto;
import com.hotel.hotel_stars.Entity.Room;
import com.hotel.hotel_stars.Entity.TypeBed;
import com.hotel.hotel_stars.Entity.TypeRoom;
import com.hotel.hotel_stars.Entity.TypeRoomImage;
import com.hotel.hotel_stars.Repository.RoomRepository;
import com.hotel.hotel_stars.Repository.TypeBedRepository;
import com.hotel.hotel_stars.Repository.TypeRoomImageRepository;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TypeRoomOverviewService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private TypeRoomImageRepository typeRoomImageRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    TypeBedRepository typeBedRepository;

    public TypeRoomImageDto convertToDto(TypeRoomImage typeRoomImage) {
        // Map other fields from TypeRoomImage to TypeRoomImageDto, set typeRoomDto to null
        return new TypeRoomImageDto(
                typeRoomImage.getId(),
                typeRoomImage.getImageName(),
                null  // Set typeRoomDto to null
        );
    }

    public List<TypeRoomOverviewDTO> getTypeRoomOverview() {
        List<TypeRoomOverviewDTO> typeRoomOverviewDTOList = new ArrayList<>();
        List<Object[]> roomList = roomRepository.getRoomTypeData();

        roomList.forEach(row -> {
            TypeRoomOverviewDTO typeRoomOverviewDTO = new TypeRoomOverviewDTO();
            Long roomCount = (Long) row[2];
            Integer imageId = (Integer) row[8];

            typeRoomOverviewDTO.setId((Integer) row[0]);
            typeRoomOverviewDTO.setTypeRoomName((String) row[1]);
            typeRoomOverviewDTO.setRoomCount(roomCount.intValue());
            typeRoomOverviewDTO.setPrice((Double) row[3]);
            Optional<TypeBed> optional = typeBedRepository.findById((Integer) row[4]);
            typeRoomOverviewDTO.setTypeBed(convertDto(optional.get()));
            typeRoomOverviewDTO.setBedCount((Integer) row[5]);
            typeRoomOverviewDTO.setGuestLimit((Integer) row[6]);
            typeRoomOverviewDTO.setAcreage((Double) row[7]);

            // Fetch TypeRoomImage by ID and convert to DTO
            TypeRoomImage typeRoomImage = typeRoomImageRepository.findById(imageId)
                    .orElse(null); // or handle missing image appropriately

            if (typeRoomImage != null) {
                TypeRoomImageDto typeRoomImageDto = convertToDto(typeRoomImage);
                typeRoomOverviewDTO.setTypeRoomImage(typeRoomImageDto);
            }
            typeRoomOverviewDTOList.add(typeRoomOverviewDTO);
        });

        return typeRoomOverviewDTOList;
    }

    public List<RoomDto> seleteTypeRoom(Integer IdTypeRoom) {
        List<Room> listRoom = roomRepository.findByTypeRoomId(IdTypeRoom);
        return listRoom.stream().map((element) -> modelMapper.map(element, RoomDto.class)).toList();
    }

    public List<RoomInfoDTO> getAllListRoom() {
        List<Object[]> roomList = roomRepository.findAllRoomInfo();
        List<RoomInfoDTO> roomInfoDTOList = new ArrayList<>();
        roomList.forEach(row -> {
            RoomInfoDTO roomInfoDTO = new RoomInfoDTO();
            roomInfoDTO.setRoomName((String) row[0]);
            roomInfoDTO.setTypeRoomName((String) row[1]);
            roomInfoDTO.setFloorName((String) row[2]);
            roomInfoDTO.setStatusRoomName((String) row[3]);
            roomInfoDTO.setRoomId((Integer) row[4]);
            roomInfoDTO.setTypeRoomId((Integer) row[5]);
            roomInfoDTO.setStatusRoomId((Integer) row[6]);
            roomInfoDTOList.add(roomInfoDTO);
        });
        return roomInfoDTOList;
    }

    public List<RoomReservationDTO> getRoomReservationList(Integer IdTypeRoom) {
        List<Object[]> list = roomRepository.findBookingsByTypeRoomIdOrderedByCheckIn(IdTypeRoom);
        List<RoomReservationDTO> roomReservationDTOList = new ArrayList<>();
        list.forEach(row -> {
            RoomReservationDTO dto = new RoomReservationDTO(
                    (String) row[0],  // room_name
                    (String) row[1],  // type_room_name
                    ((java.sql.Timestamp) row[2]).toLocalDateTime(),  // check_in
                    ((java.sql.Timestamp) row[3]).toLocalDateTime(),  // check_out
                    (String) row[4],  // guest_name
                    (String) row[5]   // status_room_name
            );
            roomReservationDTOList.add(dto);
        });
        return roomReservationDTOList;
    }

    public TypeBedDto convertDto(TypeBed typeBed) {
        TypeBedDto typeBedDto = modelMapper.map(typeBed, TypeBedDto.class);
        return typeBedDto;
    }

    public List<TypeBedDto> getTypeBedList() {
        List<TypeBed> listTypeBed = typeBedRepository.findAll();
        return listTypeBed.stream().map(this::convertDto).toList();
    }
}
