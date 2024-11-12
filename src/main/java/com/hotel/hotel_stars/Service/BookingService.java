package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.Config.JwtService;
import com.hotel.hotel_stars.DTO.Select.BookingDetailDTO;
import com.hotel.hotel_stars.DTO.Select.CustomerReservation;
import com.hotel.hotel_stars.DTO.Select.PaymentInfoDTO;
import com.hotel.hotel_stars.DTO.Select.ReservationInfoDTO;
import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.Entity.*;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Exception.ErrorsService;
import com.hotel.hotel_stars.Models.bookingModel;
import com.hotel.hotel_stars.Repository.*;
import com.hotel.hotel_stars.utils.paramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ErrorsService errorsService;
    @Autowired
    private TypeRoomRepository typeRoomRepository;
    @Autowired
    private MethodPaymentRepository methodPaymentRepository;

    @Autowired
    private StatusBookingRepository statusBookingRepository;
    @Autowired
    JwtService jwtService;
    @Autowired
    private paramService paramServices;

    public List<BookingDetailDTO> getBookingDetailsByAccountId(Integer accountId) {
        List<Object[]> results = bookingRepository.findBookingDetailsByAccountId(accountId);
        List<BookingDetailDTO> bookingDetails = new ArrayList<>();

        for (Object[] result : results) {
            Integer bookingId = (Integer) result[0];
            String typeRoomName = (String) result[1];
            String roomName = (String) result[2];
            Instant checkIn = (Instant) result[3];
            Instant checkOut = (Instant) result[4];
            Integer numberOfDays = (Integer) result[5];

            BookingDetailDTO dto = new BookingDetailDTO(bookingId, typeRoomName, roomName, checkIn, checkOut, numberOfDays);
            bookingDetails.add(dto);
        }
        return bookingDetails;
    }

    public List<PaymentInfoDTO> getPaymentInfoByAccountId(Integer accountId) {
        List<Object[]> results = bookingRepository.findPaymentInfoByAccountId(accountId);
        List<PaymentInfoDTO> paymentInfoDTOs = new ArrayList<>();
        for (Object[] result : results) {
            String methodPaymentName = (String) result[0];
            Boolean status = (Boolean) result[1];
            Double amount = (Double) result[2];

            PaymentInfoDTO paymentInfoDTO = new PaymentInfoDTO(methodPaymentName, status, amount);
            paymentInfoDTOs.add(paymentInfoDTO);
        }
        return paymentInfoDTOs;
    }

//    public Boolean sendBookingEmail(bookingModel bookingModels) {
//        Booking booking = new Booking();
//        Optional<Account> account = accountRepository.findByUsername(bookingModels.getUsername());
//        Optional<TypeRoom> typeRoom = typeRoomRepository.findById(bookingModels.getIdTypeRoom());
//        Optional<MethodPayment> methodPayment = methodPaymentRepository.findById(1);
//        Optional<StatusBooking> statusBooking = statusBookingRepository.findById(1);
//        try {
//            booking.setAccount(account.get());
//            booking.setTypeRoom(typeRoom.get());
//            booking.setStartAt(paramServices.stringToInstant(bookingModels.getStartDate()));
//            booking.setEndAt(paramServices.stringToInstant(bookingModels.getEndDatel()));
//            booking.setCreateAt(Instant.now());
//            booking.setStatusPayment(false);
//            booking.setMethodPayment(methodPayment.get());
//            booking.setStatus(statusBooking.get());
//            bookingRepository.save(booking);
//            String emailContent = "Click vào đây: <a href=\"" + "http://localhost:8080/api/booking/confirmBooking?token=" +
//                    jwtService.generateBoking(booking.getId(), bookingModels.getQuantityRoom()) + "\">Xác Nhận Đặt phòng</a>";
//
//            paramServices.sendEmails(account.get().getEmail(), "Xác Nhận Đặt phòng", emailContent);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    public List<ReservationInfoDTO> getAllReservationInfoDTO() {
        List<Object[]> results = bookingRepository.findAllBookingDetailsUsingSQL();
        List<ReservationInfoDTO> dtos = new ArrayList<>();
        for (Object[] row : results) {
            Integer bookingId = (Integer) row[0];
            Integer accountId = (Integer) row[1];
            Integer statusBookingId = (Integer) row[2];
            Integer methodPaymentId = (Integer) row[3];
            Integer bookingRoomId = (Integer) row[4];
            Integer roomId = (Integer) row[5];
            Integer typeRoomId = (Integer) row[6];
            Integer invoiceId = (Integer) row[7];
            String roomName = (String) row[8];
            String methodPaymentName = (String) row[9];
            String statusRoomName = (String) row[10];
            String statusBookingName = (String) row[11];
            Timestamp timestampCreateAt = (Timestamp) row[12];
            LocalDateTime createAt = timestampCreateAt.toLocalDateTime();

            Timestamp timestampStartAt = (Timestamp) row[13];
            LocalDateTime startAt = timestampStartAt.toLocalDateTime();

            Timestamp timestampEndAt = (Timestamp) row[14];
            LocalDateTime endAt = timestampEndAt.toLocalDateTime();
            String accountFullname = (String) row[15];
            String roleName = (String) row[16];
            String typeRoomName = String.valueOf(row[17]);
            Double total_amount = (Double) row[18];
            Integer max_guests = (Integer) row[19];
            // Add to DTO list
            dtos.add(new ReservationInfoDTO(bookingId, accountId, statusBookingId, methodPaymentId,
                    bookingRoomId, roomId, typeRoomId, invoiceId, roomName,
                    methodPaymentName, statusRoomName, statusBookingName,
                    createAt, startAt, endAt, accountFullname, roleName,
                    typeRoomName, total_amount, max_guests
            ));
        }
        return dtos;
    }

    public CustomerReservation mapToCustomerReservation(Integer bookingId) {
        // Gọi phương thức trong repository
        Optional<CustomerReservation> customerReservation = bookingRepository.findBookingDetailsById(bookingId);

        // Kiểm tra nếu có kết quả
        if (customerReservation.isPresent()) {
            // Nếu có, trả về CustomerReservation
            return customerReservation.get();
        } else {
            // Nếu không có kết quả, có thể ném ngoại lệ hoặc trả về null
            throw new RuntimeException("Booking not found with id " + bookingId);
        }
    }

    public StatusResponseDto updateBookingStatus(Integer bookingId) {
        try {
            // Kiểm tra nếu không tìm thấy booking
            Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
            if (optionalBooking.isEmpty()) {
                return new StatusResponseDto("404", "error", "Không tìm thấy đơn đặt phòng với ID " + bookingId);
            }

            // Tìm status, nếu không có thì ném ngoại lệ
            StatusBooking statusBooking = statusBookingRepository.findById(6)
                    .orElseThrow(null);

            // Cập nhật trạng thái
            Booking booking = optionalBooking.get();
            booking.setStatus(statusBooking);
            bookingRepository.save(booking);

            return new StatusResponseDto("200", "success", "Cập nhật trạng thái thành công cho đơn đặt phòng");
        } catch (RuntimeException e) {
            // Bắt lỗi ngoại lệ và trả về phản hồi chi tiết
            return new StatusResponseDto("500", "error", "Đã xảy ra lỗi: " + e.getMessage());
        } catch (Exception e) {
            // Bắt lỗi không xác định khác
            return new StatusResponseDto("500", "error", "Đã xảy ra lỗi không xác định: " + e.getMessage());
        }
    }
}
