package com.hotel.hotel_stars.Exception;

import com.hotel.hotel_stars.Entity.Account;
import com.hotel.hotel_stars.Entity.TypeRoom;
import com.hotel.hotel_stars.Models.bookingModel;
import com.hotel.hotel_stars.Repository.AccountRepository;
import com.hotel.hotel_stars.Repository.TypeRoomRepository;
import com.hotel.hotel_stars.utils.paramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ErrorsService {
    @Autowired
    paramService paramServices;
    @Autowired
    TypeRoomRepository typeRoomRepository;
    @Autowired
    AccountRepository accountRepository;
    public void errorBooking(bookingModel bookingModels) throws CustomValidationException {
        List<ValidationError> errors = new ArrayList<>();
        Instant startInstant=paramServices.stringToInstant(bookingModels.getStartDate());
        Instant endInstant=paramServices.stringToInstant(bookingModels.getEndDatel());
        Optional<TypeRoom> typeRoom=typeRoomRepository.findById(bookingModels.getIdTypeRoom());
        Optional<Account> account=accountRepository.findByUsername(bookingModels.getUsername());
        if(!account.isPresent()){
            errors.add(new ValidationError("username", "người dùng này không tồn tại"));
        }
        if(!typeRoom.isPresent()){
            errors.add(new ValidationError("typeRoom", "loại phòng này không tồn tại"));
        }
        if(bookingModels.getQuantityRoom()<=0){
            errors.add(new ValidationError("quantityRoom", "số lượng phòng không được bé hơn hoặc bằng 0"));
        }
        if (startInstant.isAfter(endInstant)) {
            errors.add(new ValidationError("startdate", "Ngày Bắt đầu không nhỏ hơn ngày kết thúc"));
        }
        if (!errors.isEmpty()) {
            throw new CustomValidationException(errors);
        }
    }
}
