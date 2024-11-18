package com.hotel.hotel_stars.Exception;

import com.hotel.hotel_stars.Repository.TypeRoomRepository;
import com.hotel.hotel_stars.Utils.paramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ErrorsService {
    @Autowired
    paramService paramServices;
    @Autowired
    TypeRoomRepository typeRoomRepository;

}
