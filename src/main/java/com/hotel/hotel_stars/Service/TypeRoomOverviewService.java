package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.RoomDto;
import com.hotel.hotel_stars.DTO.Select.RoomInfoDTO;
import com.hotel.hotel_stars.DTO.Select.TypeRoomOverviewDTO;
import com.hotel.hotel_stars.DTO.TypeRoomDto;
import com.hotel.hotel_stars.DTO.TypeRoomImageDto;
import com.hotel.hotel_stars.Entity.Room;
import com.hotel.hotel_stars.Entity.TypeRoom;
import com.hotel.hotel_stars.Entity.TypeRoomImage;
import com.hotel.hotel_stars.Repository.RoomRepository;
import com.hotel.hotel_stars.Repository.TypeRoomImageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TypeRoomOverviewService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private TypeRoomImageRepository typeRoomImageRepository;

    @Autowired
    private ModelMapper modelMapper;


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
            Integer imageId = (Integer) row[7];

            typeRoomOverviewDTO.setTypeId((Integer) row[0]);
            typeRoomOverviewDTO.setTypeName((String) row[1]);
            typeRoomOverviewDTO.setRoomCount(roomCount.intValue());
            typeRoomOverviewDTO.setPrice((Double) row[3]);
            typeRoomOverviewDTO.setTypeBed((Integer) row[4]);
            typeRoomOverviewDTO.setGuestLimit((String) row[5]);
            typeRoomOverviewDTO.setAcreage((Double) row[6]);

            // Fetch TypeRoomImage by ID and convert to DTO
            TypeRoomImage typeRoomImage = typeRoomImageRepository.findById(imageId)
                    .orElse(null); // or handle missing image appropriately

            if (typeRoomImage != null) {
                TypeRoomImageDto typeRoomImageDto = convertToDto(typeRoomImage);
                typeRoomOverviewDTO.setImageId(typeRoomImageDto);
            }

            typeRoomOverviewDTOList.add(typeRoomOverviewDTO);
        });

        return typeRoomOverviewDTOList;
    }

    public List<RoomDto> seleteTypeRoom(Integer IdTypeRoom) {
        List<Room> listRoom = roomRepository.findByTypeRoomId(IdTypeRoom);
        return listRoom.stream().map((element) -> modelMapper.map(element, RoomDto.class)).toList();
    }

    public List<RoomInfoDTO> getAllListRoom(){
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
}
