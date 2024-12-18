package com.hotel.hotel_stars.Exception;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.hotel.hotel_stars.Entity.Booking;
import com.hotel.hotel_stars.Entity.Invoice;
import com.hotel.hotel_stars.Models.DeleteBookingModel;
import com.hotel.hotel_stars.Models.FeedbackModel;
import com.hotel.hotel_stars.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.DTO.selectDTO.RoomAvailabilityResponse;
import com.hotel.hotel_stars.Entity.Account;
import com.hotel.hotel_stars.Entity.Room;
import com.hotel.hotel_stars.Models.bookingModel;
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
    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    BookingRepository bookingRepository;

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
        Optional<Booking> booking = Optional.empty();
        if (!account.isPresent()) {
            responseDto.setCode("400");
            responseDto.setStatus("error");
            responseDto.setMessage("Người dùng này không tồn tại");
            return responseDto;
        }
        if (!account.get().getBookingList().isEmpty()) {
            booking = account.get().getBookingList().stream()
                    .max(Comparator.comparingInt(Booking::getId));
            if (booking.isPresent()) {
                if (booking.get().getStatus().getId() == 1) {
                    responseDto.setCode("400");
                    responseDto.setStatus("error");
                    responseDto.setMessage("Đơn đặt phòng gần đây nhất chưa được bạn xác nhận. Vui lòng kiểm tra.");
                    return responseDto;
                }
                if (booking.get().getStatus().getId() == 2) {
                    responseDto.setCode("400");
                    responseDto.setStatus("error");
                    responseDto.setMessage("Đơn đặt phòng gần nhất của bạn đang chờ khách sạn xác nhận. Vui lòng đợi.");
                    return responseDto;
                }
            }
        }
        if(bookingModels.getRoomId().size()>7){
            responseDto.setCode("400");
            responseDto.setStatus("error");
            responseDto.setMessage("Vui lòng không đặt quá 7 phòng trong một lần.");
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

    public StatusResponseDto errorFeedBack(FeedbackModel feedbackModel) throws CustomValidationException {

        StatusResponseDto responseDto = new StatusResponseDto();
        Optional<Invoice> invoice =invoiceRepository.findById(feedbackModel.getIdInvoice());
        if(!invoice.isPresent()){
            responseDto.setCode("400");
            responseDto.setStatus("error");
            responseDto.setMessage("Bạn cần có hóa đơn để trả phòng và đánh giá.");
            return responseDto;
        }
        System.out.println("độ dài invoice: "+invoice.get().getFeedbackList().size());
        if(invoice.get().getFeedbackList().size()>=1){
            responseDto.setCode("400");
            responseDto.setStatus("error");
            responseDto.setMessage("Bạn đã đánh giá rồi, không được đánh giá nữa");
            return responseDto;
        }
        if(feedbackModel.getContent().isEmpty() || feedbackModel.getContent()==null){
            responseDto.setCode("400");
            responseDto.setStatus("error");
            responseDto.setMessage("Bạn cần nhập nội dung để đánh giá");
            return responseDto;
        }
        if(feedbackModel.getStars()==0 || feedbackModel.getStars()==null){
            responseDto.setCode("400");
            responseDto.setStatus("error");
            responseDto.setMessage("Chúng tôi rất mong nhận được đánh giá của bạn! Vui lòng chọn số sao.");
            return responseDto;
        }

        return null;
    }
    public StatusResponseDto errorDeleteBooking(DeleteBookingModel bookingModels) throws CustomValidationException {

        StatusResponseDto responseDto = new StatusResponseDto();
        Optional<Booking> booking =bookingRepository.findById(bookingModels.getBookingId());
        if(!booking.isPresent()){
            responseDto.setCode("400");
            responseDto.setStatus("error");
            responseDto.setMessage("Đơn đặt phòng này không tồn tại");
            return responseDto;
        }
        Integer statusID=booking.get().getStatus().getId();
        if (statusID != 1 && statusID != 2) {
            responseDto.setCode("400");
            responseDto.setStatus("error");
            responseDto.setMessage("Đơn đặt phòng này không thể hủy, Vui lòng liên hệ khách sạn!");
            return responseDto;
        }
        if(bookingModels.getDescriptions().isEmpty() || bookingModels.getDescriptions()==null){
            responseDto.setCode("400");
            responseDto.setStatus("error");
            responseDto.setMessage("Bạn phải có lý do để hủy phòng!");
            return responseDto;

        }

        return null;
    }

}