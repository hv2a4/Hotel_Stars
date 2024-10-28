package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.selectDTO.FindTypeRoomDto;
import com.hotel.hotel_stars.Repository.TypeRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TypeRoomService {
    @Autowired
    TypeRoomRepository typeRoomRepository;

    public List<FindTypeRoomDto> getFindTypeRoom() {
        LocalDate startDate = LocalDate.parse("2023-10-28");
        LocalDate endDate = LocalDate.parse("2023-10-29");
        List<Object[]> results = typeRoomRepository.findAllTypeRoomDetailsWithCost(startDate, endDate);
        List<FindTypeRoomDto> dtoList = new ArrayList<>();

        results.stream().forEach(row -> {
            String typeRoomName = (String) row[0];
            Double acreage = (Double) row[1];
            Integer capacity = (Integer) row[2];
            String amenitiesTypeRoomName = (String) row[3];
            Double estCost = (Double) row[4];

            // Kiểm tra xem DTO đã tồn tại trong danh sách chưa bằng Stream API
            FindTypeRoomDto existingDto = dtoList.stream()
                    .filter(dto -> dto.getTypeRoomName().equals(typeRoomName))
                    .findFirst()
                    .orElse(null);

            if (existingDto == null) {
                // Nếu chưa có DTO cho loại phòng này, tạo mới
                existingDto = new FindTypeRoomDto(typeRoomName, acreage, capacity, new ArrayList<>(), estCost);
                dtoList.add(existingDto);
            }

            // Thêm tiện nghi vào danh sách
            existingDto.getAmenitiesTypeRoomNames().add(amenitiesTypeRoomName);
        });

        return dtoList; // Trả về danh sách DTO
    }
}
