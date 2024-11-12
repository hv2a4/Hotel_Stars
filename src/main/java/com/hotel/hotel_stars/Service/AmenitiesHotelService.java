package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.HotelDto;
import com.hotel.hotel_stars.Entity.AmenitiesHotel;
import com.hotel.hotel_stars.Entity.Hotel;
import com.hotel.hotel_stars.Repository.AmenitiesHotelRepository;
import com.hotel.hotel_stars.Repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AmenitiesHotelService {
    @Autowired
    private AmenitiesHotelRepository amenitiesHotelRepository;

    @Autowired
    private HotelRepository hotelRepository;

    // Tìm kiếm

//    public AmenitiesHotel convertAmenitiesHotelDto(AmenitiesHotel amenitiesHotel) {
//        HotelDto htdto = new HotelDto();
//        htdto.setId(amenitiesHotel.getHotel().getId());
//        htdto.setHotelName(amenitiesHotel.getHotel().getHotelName());
//        ht
//    }
    // Hiện danh sách
//    public
}
