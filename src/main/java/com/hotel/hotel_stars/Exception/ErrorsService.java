package com.hotel.hotel_stars.Exception;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.hotel.hotel_stars.Entity.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.DTO.selectDTO.RoomAvailabilityResponse;
import com.hotel.hotel_stars.Entity.Account;
import com.hotel.hotel_stars.Entity.Room;
import com.hotel.hotel_stars.Models.bookingModel;
import com.hotel.hotel_stars.Repository.AccountRepository;
import com.hotel.hotel_stars.Repository.RoomRepository;
import com.hotel.hotel_stars.Repository.TypeRoomRepository;
import com.hotel.hotel_stars.utils.paramService;

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
        LocalDate start = paramServices.convertStringToLocalDate(startDate);
        LocalDate end = paramServices.convertStringToLocalDate(endDate);

        for (Integer roomId : roomIds) {
            Long count = typeRoomRepository.countAvailableRoom(roomId, start, end);
            // If the count is 0, the room is not available
            System.out.println("đếm: " + count);
            if (count == 0) {
                // Return false and the ID of the unavailable room
                return new RoomAvailabilityResponse(false, roomId);
            }
        }
        return new RoomAvailabilityResponse(true, null);
    }

    public StatusResponseDto errorBooking(bookingModel bookingModels) throws CustomValidationException {
        List<ValidationError> errors = new ArrayList<>();
        StatusResponseDto responseDto = new StatusResponseDto();
        Instant startInstant = paramServices.stringToInstant(bookingModels.getStartDate());
        Instant endInstant = paramServices.stringToInstant(bookingModels.getEndDate());
        Optional<Account> account = accountRepository.findByUsername(bookingModels.getUserName());
        RoomAvailabilityResponse response = isRoomAvailable(bookingModels.getRoomId(), bookingModels.getStartDate(), bookingModels.getEndDate());
        System.out.println(response.isAllRoomsAvailable());


        System.out.println(account.get().getBookingList().getLast());
        Optional<Booking> booking = account.get().getBookingList().stream()
                .max(Comparator.comparingInt(Booking::getId));


        if(booking.get().getStatus().getId()==1){
            responseDto.setCode("400");
            responseDto.setStatus("error");
            responseDto.setMessage("Đơn đặt phòng gần đây nhất chưa được bạn xác nhận. Vui lòng kiểm tra.");
            return responseDto;
        }
        if(booking.get().getStatus().getId()==2){
            responseDto.setCode("400");
            responseDto.setStatus("error");
            responseDto.setMessage("Đơn đặt phòng gần nhất của bạn đang chờ khách sạn xác nhận. Vui lòng đợi.");
            return responseDto;
        }
        if(bookingModels.getRoomId().size()>7){
            responseDto.setCode("400");
            responseDto.setStatus("error");
            responseDto.setMessage("Vui lòng không đặt quá 7 phòng trong một lần.");
            return responseDto;
        }
        if (!account.isPresent()) {
            responseDto.setCode("400");
            responseDto.setStatus("error");
            responseDto.setMessage("Người dùng này không tồn tại");
            return responseDto;
        }
        if (account.get().getPhone() == null || account.get().getPhone().isEmpty()) {
            responseDto.setCode("400");
            responseDto.setStatus("error");
            responseDto.setMessage("Người dùng chưa có số điện thoại");
            return responseDto;
        }
        if (bookingModels.getRoomId().size() <= 0) {
            responseDto.setCode("400");
            responseDto.setStatus("error");
            responseDto.setMessage("Số lượng phòng không được bé hơn hoặc bằng 0");
            return responseDto;
        }
        if (startInstant.isAfter(endInstant)) {
            responseDto.setCode("400");
            responseDto.setStatus("error");
            responseDto.setMessage("Ngày Bắt đầu không nhỏ hơn ngày kết thúc");
            return responseDto;
        }
        if (response.isAllRoomsAvailable() == false) {
            Room rooms = roomRepository.findById(response.getUnavailableRoomId()).get();
            errors.add(new ValidationError("room", "Id: " + rooms.getId() + ", " + "Phòng: " + rooms.getRoomName() + ", Loại phòng: " + rooms.getTypeRoom().getTypeRoomName() + ", Đã có người đặt rồi"));
            responseDto.setCode("400");
            responseDto.setStatus("error");
            responseDto.setMessage("Từ: " + bookingModels.getStartDate() + " đến " + bookingModels.getEndDate() + "," + rooms.getRoomName()+", Đã có người đặt rồi");
            return responseDto;
        }

        return null;
    }

}