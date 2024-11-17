package com.hotel.hotel_stars.Exception;

import com.hotel.hotel_stars.Entity.Account;
import com.hotel.hotel_stars.Entity.Booking;
import com.hotel.hotel_stars.Entity.BookingRoom;
import com.hotel.hotel_stars.Entity.TypeRoom;
import com.hotel.hotel_stars.Models.bookingModel;
import com.hotel.hotel_stars.Repository.AccountRepository;
import com.hotel.hotel_stars.Repository.TypeRoomRepository;
import com.hotel.hotel_stars.Service.BookingService;
import com.hotel.hotel_stars.Utils.paramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class ErrorsService {
    @Autowired
    paramService paramServices;
    @Autowired
    TypeRoomRepository typeRoomRepository;

}
