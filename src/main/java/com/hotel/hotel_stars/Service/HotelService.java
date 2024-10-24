package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.AccountDto;
import com.hotel.hotel_stars.DTO.HotelDto;
import com.hotel.hotel_stars.Entity.Account;
import com.hotel.hotel_stars.Entity.Hotel;
import com.hotel.hotel_stars.Repository.HotelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class HotelService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    HotelRepository hotelRepository;
    public HotelDto convertHotelDto(Hotel hotel){
        return  new HotelDto(hotel.getId(), hotel.getHotelName(), hotel.getDescriptions(),
                hotel.getProvince(), hotel.getDistrict(), hotel.getWard(), hotel.getAddress());
    }
    public List<HotelDto> getAllHotels() {
        List<Hotel> hotels=hotelRepository.findAll();
        return hotels.
                stream()
                .map(this::convertHotelDto)
                .toList();
    }
}
