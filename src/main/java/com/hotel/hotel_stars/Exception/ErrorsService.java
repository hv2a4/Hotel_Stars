package com.hotel.hotel_stars.Exception;

import com.hotel.hotel_stars.Repository.TypeRoomRepository;
import com.hotel.hotel_stars.Service.BookingService;
import com.hotel.hotel_stars.utils.paramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ErrorsService {
    @Autowired
    paramService paramServices;
    @Autowired
    TypeRoomRepository typeRoomRepository;

}
