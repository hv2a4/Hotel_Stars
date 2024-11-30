package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.Config.JwtService;
import com.hotel.hotel_stars.Config.VNPayService;
import com.hotel.hotel_stars.DTO.accountHistoryDto;
import com.hotel.hotel_stars.DTO.Select.BookingDetailDTO;
import com.hotel.hotel_stars.DTO.Select.PaymentInfoDTO;
import com.hotel.hotel_stars.Entity.Booking;
import com.hotel.hotel_stars.Entity.BookingRoom;
import com.hotel.hotel_stars.Entity.StatusBooking;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Exception.ErrorsService;
import com.hotel.hotel_stars.Models.bookingModel;
import com.hotel.hotel_stars.Models.bookingModelNew;
import com.hotel.hotel_stars.Models.bookingRoomModel;
import com.hotel.hotel_stars.Repository.BookingRepository;
import com.hotel.hotel_stars.Repository.BookingRoomRepository;
import com.hotel.hotel_stars.Repository.StatusBookingRepository;
import com.hotel.hotel_stars.Service.BookingService;
import com.hotel.hotel_stars.utils.SessionService;
import com.hotel.hotel_stars.utils.paramService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/booking")
public class BookingController {
    @Autowired
    private BookingService bookingService;
    @Lazy
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
    @Autowired
    VNPayService vnPayService;
    @Autowired
    SessionService sessionService;

    // khoi
    @GetMapping("")
    public ResponseEntity<List<accountHistoryDto>> getBookings(
            @RequestParam(required = false) String filterType,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        List<accountHistoryDto> bookings = bookingService.getAllBooking(filterType, startDate, endDate);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/update-status/{id}/{idStatus}")
    public ResponseEntity<?> updateStatus(@PathVariable("id") Integer idBooking,
                                          @PathVariable("idStatus") Integer idStatus, @RequestBody bookingModelNew bookingModel) {
        // Gọi phương thức updateStatusBooking từ service
        Map<String, String> response = new HashMap<String, String>();
        boolean update = bookingService.updateStatusBooking(idBooking, idStatus, bookingModel);

        if (update == true) {
            response = paramServices.messageSuccessApi(201, "success",
                    "Cập nhật trạng thái thành công");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response = paramServices.messageSuccessApi(400, "error", "Cập nhật trạng thái thất bại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/update-checkIn/{id}")
    public ResponseEntity<?> updateCheckIn(@PathVariable("id") Integer id,
                                           @RequestParam("roomId") List<Integer> roomId,
                                           @RequestBody List<bookingRoomModel> model) {
        Map<String, String> response = new HashMap<String, String>();
        boolean update = bookingService.updateStatusCheckInBooking(id, roomId, model);

        if (update == true) {
            response = paramServices.messageSuccessApi(201, "success",
                    "Cập nhật trạng thái thành công");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response = paramServices.messageSuccessApi(400, "error", "Cập nhật trạng thái thất bại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(bookingService.getByIdBooking(id));
    }


    // khoi

    @GetMapping("/account/payment-info/{id}")
    public ResponseEntity<List<PaymentInfoDTO>> getBookingPaymentInfo(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.getPaymentInfoByAccountId(id));
    }

    @PostMapping("/sendBooking")
    public ResponseEntity<?> postBooking(@Valid @RequestBody bookingModel bookingModels, HttpServletRequest request) {
        Map<String, String> response = new HashMap<String, String>();

        errorsServices.errorBooking(bookingModels);

        try {
            Booking bookings = bookingService.sendBookingEmail(bookingModels);
            if (bookings != null) {
                response = paramServices.messageSuccessApi(201, "success",
                        "Đặt phòng thành công, vui lòng vào email để xác nhận");
                if (bookings.getMethodPayment().getId() == 1) {
                    response.put("vnPayURL", null);
                } else {
                    List<BookingRoom> bookingRoomList = bookings.getBookingRooms();
                    double total = bookingRoomList.stream().mapToDouble(BookingRoom::getPrice).sum();
                    int totalAsInt = (int) total;
                    System.out.println("session được lưu: " + Optional.ofNullable(sessionService.get("booking")));
                    System.out.println("Session ID1: " + sessionService.getIdSession());
                    String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
                    response = paramServices.messageSuccessApi(201, "success",
                            "Đặt phòng thành công");
                    response.put("vnPayURL", vnPayService.createOrder(totalAsInt, String.valueOf(bookings.getId()), baseUrl));
                }
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                response = paramServices.messageSuccessApi(400, "error", "Đặt phòng thất bại");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            response.put("error", e.getMessage()); // Thêm thông tin lỗi ở đây
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/booking-offline")
    public ResponseEntity<?> postBookingOffline(@Valid @RequestBody bookingModel bookingModels) {
        Map<String, String> response = new HashMap<String, String>();
        errorsServices.errorBooking(bookingModels);
        Boolean flag = bookingService.addBookingOffline(bookingModels);
        if (flag == true) {
            response = paramServices.messageSuccessApi(201, "success", "Đặt phòng thành công");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response = paramServices.messageSuccessApi(400, "error", "Đặt phòng thất bại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/confirmBooking")
    public ResponseEntity<?> updateBooking(@RequestParam("token") String token) {

        try {
            Integer id = jwtService.extractBookingId(token);
            Optional<StatusBooking> statusBooking = statusBookingRepository.findById(2);
            Booking booking = bookingRepository.findById(id).get();
            List<BookingRoom> bookingRoomList = booking.getBookingRooms();
            double total = bookingRoomList.stream().mapToDouble(BookingRoom::getPrice).sum();
            String formattedAmount = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(total);
            LocalDate startDate = paramServices.convertInstallToLocalDate(booking.getStartAt());
            LocalDate endDate = paramServices.convertInstallToLocalDate(booking.getEndAt());
            booking.setStatus(statusBooking.get());
            String roomsString = bookingRoomList.stream()
                    .map(bookingRoom -> bookingRoom.getRoom().getRoomName())  // Extract roomName from each BookingRoom
                    .collect(Collectors.joining(", "));
            String idBk = "Bk" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "" + booking.getId();
            System.out.println("chuỗi: " + roomsString);
            try {
                bookingRepository.save(booking);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok(paramServices.confirmBookings(idBk, booking, startDate, endDate, formattedAmount, roomsString));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token đã hết hạn. Vui lòng liên lạc qua số điện thoại 1900 6522");
        } catch (Exception e) {
            // Xử lý các ngoại lệ khác nếu cần
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Đã có lỗi xảy ra.");
        }
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<BookingDetailDTO>> getBookingDetails(@PathVariable Integer accountId) {
        return ResponseEntity.ok(bookingService.getBookingDetailsByAccountId(accountId));
    }

    @GetMapping("/accountId/{id}")
    public ResponseEntity<?> getBookingByAccount(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(bookingService.getListByAccountId(id));
    }


}
