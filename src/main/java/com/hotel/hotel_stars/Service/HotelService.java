package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.HotelDto;
import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.Entity.Hotel;
import com.hotel.hotel_stars.Models.HotelModel;
import com.hotel.hotel_stars.Repository.HotelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    HotelRepository hotelRepository;

    public HotelDto convertHotelDto(Hotel hotel) {
        return new HotelDto(hotel.getId(), hotel.getHotelName(), hotel.getDescriptions(),
                hotel.getProvince(), hotel.getDistrict(), hotel.getWard(), hotel.getAddress(), hotel.getHotelPhone());
    }

    public HotelDto getAllHotels() {
        List<Hotel> hotels = hotelRepository.findAll();
        return hotels.stream()
                .findFirst() // Lấy khách sạn đầu tiên
                .map(this::convertHotelDto) // Chuyển đổi thành HotelDto
                .orElse(null); // Trả về null nếu không có khách sạn
    }
    public HotelDto getHotel(){
        Optional<Hotel> hotel = hotelRepository.findById(1);
        return convertHotelDto(hotel.get());
    }
    public StatusResponseDto updateHotel(HotelModel hotelModel) {
        Optional<Hotel> optionalHotel = hotelRepository.findById(hotelModel.getId());

        if (!optionalHotel.isPresent()) {
            // Return 404 Not Found response details
            return new StatusResponseDto("404", "Not Found", "Khách sạn không tồn tại");
        }

        Hotel hotel = optionalHotel.get();

        try {
            // Update hotel details
            hotel.setHotelName(hotelModel.getHotelName());
            hotel.setDescriptions(hotelModel.getDescriptions());
            hotel.setProvince(hotelModel.getProvince());
            hotel.setDistrict(hotelModel.getDistrict());
            hotel.setWard(hotelModel.getWard());
            hotel.setAddress(hotelModel.getAddress());

            // Save changes
            hotelRepository.save(hotel);

            // Return 200 OK response details
            return new StatusResponseDto("200", "Success", "Cập nhật dữ liệu thành công");
        } catch (Exception e) {
            e.printStackTrace();
            // Return 500 Internal Server Error response details
            return new StatusResponseDto("500", "Internal Server Error", "Đã xảy ra lỗi: " + e.getMessage());
        }
    }
}
