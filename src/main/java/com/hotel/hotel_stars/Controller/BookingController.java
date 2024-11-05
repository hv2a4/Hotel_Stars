package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.Config.JwtService;
import com.hotel.hotel_stars.DTO.Select.BookingDetailDTO;
import com.hotel.hotel_stars.DTO.Select.PaymentInfoDTO;
import com.hotel.hotel_stars.Entity.Booking;
import com.hotel.hotel_stars.Entity.BookingRoom;
import com.hotel.hotel_stars.Entity.StatusBooking;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Exception.ErrorsService;
import com.hotel.hotel_stars.Models.bookingModel;
import com.hotel.hotel_stars.Repository.BookingRepository;
import com.hotel.hotel_stars.Repository.BookingRoomRepository;
import com.hotel.hotel_stars.Repository.StatusBookingRepository;
import com.hotel.hotel_stars.Service.BookingService;
import com.hotel.hotel_stars.utils.paramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/booking/")
public class BookingController {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private ErrorsService errorsServices;
    @Autowired
    private BookingRoomRepository bookingRoomRepository;
    @Autowired
    private StatusBookingRepository statusBookingRepository;
    @Autowired
    private paramService paramServices;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private BookingRepository bookingRepository;
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<BookingDetailDTO>> getBookingDetails(@PathVariable Integer accountId) {
        return ResponseEntity.ok(bookingService.getBookingDetailsByAccountId(accountId)) ;
    }

    @GetMapping("/account/payment-info/{id}")
    public ResponseEntity<List<PaymentInfoDTO>> getBookingPaymentInfo(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.getPaymentInfoByAccountId(id));
    }
    @PostMapping ("/sendBooking")
    public ResponseEntity<?> postBooking(@RequestBody bookingModel bookingModels) {
        Map<String, String> response = new HashMap<>();
        Boolean checks=bookingService.sendBookingEmail(bookingModels);
        errorsServices.errorBooking(bookingModels);
        try{
            if(!checks){
                response=  paramServices.messageSuccessApi(400,"error","lỗi đặt phòng");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }else {
                response=  paramServices.messageSuccessApi(200,"success","Đặt phòng thành công, vui lòng vào email để xác nhận đặt phòng");
                return ResponseEntity.ok(response);
            }
        }catch (CustomValidationException e) {
            StringBuilder errorMessages = new StringBuilder();
            e.getErrorMessages().forEach(error ->
                    errorMessages.append("Field: ").append(error.getField()).append(", Message: ").append(error.getMessage()).append("; ")
            );
            response = paramServices.messageSuccessApi(400, "validation_error", errorMessages.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @GetMapping ("/confirmBooking")
    public ResponseEntity<?> updateBooking(@RequestParam("token") String token) {
        try {
            Optional<Booking> booking=bookingRepository.findById(jwtService.extractBookingId(token));
            Integer quantityRoom = jwtService.extractQuantityRoom(token);
            Optional<StatusBooking> statusBooking=statusBookingRepository.findById(2);
            booking.get().setStatus(statusBooking.get());
            bookingRepository.save(booking.get());

            for (int i = 0; i < quantityRoom; i++) { // Chú ý rằng điều kiện là < chứ không phải <=
                BookingRoom bookingRoom = new BookingRoom(); // Tạo đối tượng mới trong mỗi vòng lặp
                bookingRoom.setBooking(booking.get());
                bookingRoom.setPrice(booking.get().getTypeRoom().getPrice());
                bookingRoomRepository.save(bookingRoom);
                System.out.println(i + " số i");
            }

            String generateHtmls = paramServices.generateInvoice(booking.get(),quantityRoom,
                    (Double) (quantityRoom * booking.get().getTypeRoom().getPrice()),
                    booking.get().getTypeRoom().getPrice());

            return ResponseEntity.ok(generateHtmls);
        } catch (Exception e) {
            // Log the error
            System.err.println("Error processing confirmBooking: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing your request.");
        }
    }
}
