package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.FloorDto;
import com.hotel.hotel_stars.DTO.HotelDto;
import com.hotel.hotel_stars.Entity.Floor;
import com.hotel.hotel_stars.Entity.Hotel;
import com.hotel.hotel_stars.Repository.FloorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FloorService {
    @Autowired
    private FloorRepository floorrep;

//    public HotelDto convertHotelToHotelDto(Hotel hotel) {
//        return new HotelDto(
//                hotel.getId(),
//                hotel.getHotelName(),
//                hotel.getDescriptions(),
//                hotel.getProvince(),
//                hotel.getWard(),
//                hotel.getAddress()
//        );
//    }
}
