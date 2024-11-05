package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.Config.JwtService;
import com.hotel.hotel_stars.DTO.Select.BookingDetailDTO;
import com.hotel.hotel_stars.DTO.Select.PaymentInfoDTO;
import com.hotel.hotel_stars.Entity.*;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Exception.ErrorsService;
import com.hotel.hotel_stars.Models.bookingModel;
import com.hotel.hotel_stars.Repository.*;
import com.hotel.hotel_stars.utils.paramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
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

    public Boolean sendBookingEmail(bookingModel bookingModels) {
        Booking booking = new Booking();
        Optional<Account> account = accountRepository.findByUsername(bookingModels.getUsername());
        Optional<TypeRoom> typeRoom = typeRoomRepository.findById(bookingModels.getIdTypeRoom());
        Optional<MethodPayment> methodPayment = methodPaymentRepository.findById(1);
        Optional<StatusBooking> statusBooking = statusBookingRepository.findById(1);
        try {
            booking.setAccount(account.get());
            booking.setTypeRoom(typeRoom.get());
            booking.setStartAt(paramServices.stringToInstant(bookingModels.getStartDate()));
            booking.setEndAt(paramServices.stringToInstant(bookingModels.getEndDatel()));
            booking.setCreateAt(Instant.now());
            booking.setStatusPayment(false);
            booking.setMethodPayment(methodPayment.get());
            booking.setStatus(statusBooking.get());
            bookingRepository.save(booking);
            String emailContent = "Click vào đây: <a href=\"" + "http://localhost:8080/api/booking/confirmBooking?token=" +
                    jwtService.generateBoking(booking.getId(), bookingModels.getQuantityRoom()) + "\">Xác Nhận Đặt phòng</a>";

            paramServices.sendEmails(account.get().getEmail(), "Xác Nhận Đặt phòng", emailContent);
            return true;
        }  catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}
