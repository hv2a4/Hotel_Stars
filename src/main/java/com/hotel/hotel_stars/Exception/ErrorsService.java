package com.hotel.hotel_stars.Exception;

import com.hotel.hotel_stars.DTO.selectDTO.RoomAvailabilityResponse;
import com.hotel.hotel_stars.Entity.*;
import com.hotel.hotel_stars.Models.bookingModel;
import com.hotel.hotel_stars.Repository.AccountRepository;
import com.hotel.hotel_stars.Repository.RoomRepository;
import com.hotel.hotel_stars.Repository.TypeRoomRepository;
import com.hotel.hotel_stars.Service.BookingService;
import com.hotel.hotel_stars.utils.paramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

import static java.util.Collections.*;
import static java.util.List.of;

@Service
public class ErrorsService {
    @Autowired
    paramService paramServices;
    @Autowired
    TypeRoomRepository typeRoomRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    AccountRepository accountRepository;
    private RoomAvailabilityResponse isRoomAvailable(List<Integer> roomIds, String startDate, String endDate) {
        // Convert the start and end dates to Instant
        Instant start = paramServices.stringToInstant(startDate);
        Instant end = paramServices.stringToInstant(endDate);

        // Iterate through each room ID to check availability
        for (Integer roomId : roomIds) {
            Long count = typeRoomRepository.countAvailableRoom(roomId, start, end);
            // If the count is 0, the room is not available
            System.out.println("đếm: "+count);
            if (count == 0) {
                // Return false and the ID of the unavailable room
                return new RoomAvailabilityResponse(false, roomId);
            }
        }

        // If all rooms are available (count > 0), return true with null for unavailableRoomId
        return new RoomAvailabilityResponse(true, null);
    }

    public void errorBooking(bookingModel bookingModels) throws CustomValidationException {
        List<ValidationError> errors = new ArrayList<>();
        Instant startInstant=paramServices.stringToInstant(bookingModels.getStartDate());
        Instant endInstant=paramServices.stringToInstant(bookingModels.getEndDate());
        Optional<Account> account=accountRepository.findByUsername(bookingModels.getUserName());
        RoomAvailabilityResponse response = isRoomAvailable(bookingModels.getRoomId(), bookingModels.getStartDate(), bookingModels.getEndDate());
        System.out.println(response.isAllRoomsAvailable());
        if (response.isAllRoomsAvailable() == false) {
            Room rooms=roomRepository.findById(response.getUnavailableRoomId()).get();
            errors.add(new ValidationError("room", "Id: "+rooms.getId() +", "+"Phòng: "+rooms.getRoomName()+", Loại phòng: "+rooms.getTypeRoom().getTypeRoomName() +", Đã có người đặt rồi"));
        }
        if(!account.isPresent()){
            errors.add(new ValidationError("username", "người dùng này không tồn tại"));
        }
        if(account.get().getPhone() == null || account.get().getPhone().isEmpty()){
            errors.add(new ValidationError("phone", "Người dùng chưa có số điện thoại"));
        }
        if(bookingModels.getRoomId().size()<=0){
            errors.add(new ValidationError("quantityroom", "số lượng phòng không được bé hơn hoặc bằng 0"));
        }
        if (startInstant.isAfter(endInstant)) {
            errors.add(new ValidationError("startdate", "Ngày Bắt đầu không nhỏ hơn ngày kết thúc"));
        }
        if (!errors.isEmpty()) {
            throw new CustomValidationException(errors);
        }
    }

}
