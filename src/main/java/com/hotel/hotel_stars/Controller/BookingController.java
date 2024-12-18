package com.hotel.hotel_stars.Controller;

import java.io.File;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.hotel.hotel_stars.DTO.BookingStatisticsDTO;
import com.hotel.hotel_stars.DTO.Select.RoomUsageDTO;
import com.hotel.hotel_stars.Models.DeleteBookingModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import com.hotel.hotel_stars.DTO.selectDTO.BookingHistoryDTOs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.hotel_stars.Config.JwtService;
import com.hotel.hotel_stars.Config.VNPayService;
import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.DTO.accountHistoryDto;
import com.hotel.hotel_stars.DTO.Select.BookingDetailDTO;
import com.hotel.hotel_stars.DTO.Select.PaymentInfoDTO;
import com.hotel.hotel_stars.Entity.Booking;
import com.hotel.hotel_stars.Entity.BookingRoom;
import com.hotel.hotel_stars.Entity.StatusBooking;
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
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

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
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        List<accountHistoryDto> bookings = bookingService.getAllBooking(startDate, endDate);
        return ResponseEntity.ok(bookings);
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

    @GetMapping("/account/payment-info/{id}")
    public ResponseEntity<List<PaymentInfoDTO>> getBookingPaymentInfo(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.getPaymentInfoByAccountId(id));
    }

    @PostMapping("/booking-offline")
    public ResponseEntity<?> postBookingOffline(@Valid @RequestBody bookingModel bookingModels) {
        Map<String, String> response = new HashMap<String, String>();
        errorsServices.errorBooking(bookingModels);
        accountHistoryDto flag = bookingService.addBookingOffline(bookingModels);
        if (flag != null) {
            return ResponseEntity.ok(flag);
        } else {
            return ResponseEntity.ok(flag);
        }
    }

    @GetMapping("/confirmBooking")
    public ResponseEntity<?> updateBooking(@RequestParam("token") String token) {
        // nghia, hàm này được push sáng ngày 18 tháng 12 năm 2024
        try {
            Integer id = jwtService.extractBookingId(token);
            Optional<StatusBooking> statusBooking = statusBookingRepository.findById(2);
            Booking booking = bookingRepository.findById(id).get();
            List<BookingRoom> bookingRoomList = booking.getBookingRooms();
            double total = bookingRoomList.stream().mapToDouble(BookingRoom::getPrice).sum();
            if (booking.getDiscountPercent() != null && booking.getDiscountPercent() != null) {
                double discountAmount = total * (booking.getDiscountPercent() / 100);
                total = total - discountAmount;
            }
            String formattedAmount = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(total);
            LocalDate startDate = paramServices.convertInstallToLocalDate(booking.getStartAt());
            LocalDate endDate = paramServices.convertInstallToLocalDate(booking.getEndAt());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            String startDateStr = startDate.format(formatter);
            String endDateStr = endDate.format(formatter);
            booking.setStatus(statusBooking.get());
            String roomsString = bookingRoomList.stream()
                    .map(bookingRoom -> bookingRoom.getRoom().getRoomName()) // Extract roomName from each BookingRoom
                    .collect(Collectors.joining(", "));
            String idBk = "BK" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + "TT"
                    + booking.getId();
            System.out.println("chuỗi: " + roomsString);
            try {
                bookingRepository.save(booking);
            } catch (Exception e) {
                e.printStackTrace();
            }
            paramServices.sendEmails(booking.getAccount().getEmail(), "thông tin đơn hàng",
                    paramServices.pdfDownload(idBk, booking, startDateStr, endDateStr, formattedAmount, roomsString,
                            paramServices.getImage()));
            return ResponseEntity
                    .ok(paramServices.confirmBookings(idBk, booking, startDateStr, endDateStr, formattedAmount,
                            roomsString,
                            paramServices.getImage()));
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

    @GetMapping("/downloadPdf")
    public ResponseEntity<?> downloadPdf(@RequestParam String id) {
        // nghia, hàm này được push sáng ngày 18 tháng 12 năm 2024
        try {
            Booking booking = bookingRepository.findById(Integer.parseInt(id)).get();
            List<BookingRoom> bookingRoomList = booking.getBookingRooms();
            double total = bookingRoomList.stream().mapToDouble(BookingRoom::getPrice).sum();
            if (booking.getDiscountPercent() != null && booking.getDiscountPercent() != null) {
                double discountAmount = total * (booking.getDiscountPercent() / 100);
                total = total - discountAmount;
            }
            String formattedAmount = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(total);
            LocalDate startDate = paramServices.convertInstallToLocalDate(booking.getStartAt());
            LocalDate endDate = paramServices.convertInstallToLocalDate(booking.getEndAt());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            String startDateStr = startDate.format(formatter);
            String endDateStr = endDate.format(formatter);
            String roomsString = bookingRoomList.stream()
                    .map(bookingRoom -> bookingRoom.getRoom().getRoomName()) // Extract roomName from each BookingRoom
                    .collect(Collectors.joining(", "));
            String idBk = "BK" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + "TT"
                    + booking.getId();

            paramServices.sendEmails(booking.getAccount().getEmail(), "thông tin đơn hàng",
                    paramServices.confirmBookings(idBk, booking, startDateStr, endDateStr, formattedAmount, roomsString,
                            paramServices.getImage()));

            String filePath = paramServices.generatePdf(
                    paramServices.pdfDownload(idBk, booking, startDateStr, endDateStr, formattedAmount, roomsString,
                            paramServices.getImage()),
                    booking.getAccount().getFullname(), idBk);

            File file = new File(filePath);
            System.out.println(file);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new FileSystemResource(file));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra khi tạo PDF.");
        }
    }

    @PostMapping("/sendBooking")
    public ResponseEntity<?> postBooking(@Valid @RequestBody bookingModel bookingModels, HttpServletRequest request) {
        // nghia, hàm này được push sáng ngày 18 tháng 12 năm 2024
        Map<String, String> response = new HashMap<String, String>();
        StatusResponseDto statusResponseDto = errorsServices.errorBooking(bookingModels);
        if (statusResponseDto != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusResponseDto);
        }
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
                    if (bookings.getDiscountPercent() != null && bookings.getDiscountPercent() != null) {
                        double discountAmount = total * (bookings.getDiscountPercent() / 100);
                        total = total - discountAmount;
                    }
                    int totalAsInt = (int) total;
                    System.out.println("totals: " + totalAsInt);
                    String baseUrl = request.getScheme() + "://" + request.getServerName() + ":"
                            + request.getServerPort();
                    response = paramServices.messageSuccessApi(201, "success",
                            "Đặt phòng thành công");
                    response.put("vnPayURL",
                            vnPayService.createOrder(totalAsInt, String.valueOf(bookings.getId()), baseUrl));

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

    @GetMapping("/reservation")
    public ResponseEntity<List<BookingStatisticsDTO>> getBookingStatistics(
            @RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate) {
        List<BookingStatisticsDTO> statistics = bookingService.getStatistics(startDate, endDate);
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/by-start-date-with-invoice")
    public ResponseEntity<?> getBookingsByStartAtWithInvoice(@RequestParam("date") LocalDate date) {
        return ResponseEntity.ok(bookingService.getBookingsByStartAtWithInvoice(date));
    }

    @GetMapping("/booking-history-account")
    public ResponseEntity<?> getBookings(@RequestParam Integer accountId) {
        List<BookingHistoryDTOs> bookings = bookingService.getBookingsByAccountId(accountId);
        return ResponseEntity.ok(bookings);
    }

    public List<BookingHistoryDTOs> getBookingsByAccountId(Integer accountId) {
        // Lấy danh sách dữ liệu cơ bản
        // nghia, hàm này được push sáng ngày 18 tháng 12 năm 2024
        List<Object[]> results = bookingRepository.findBookingsByAccountId(accountId);

        return results.stream()
                .map(objects -> {
                    // Lấy Booking từ repository bằng bk_id (objects[0])
                    Booking booking = bookingRepository.findById((Integer) objects[0])
                            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + objects[0]));

                    // Truy xuất thông tin bổ sung từ Booking
                    Integer methodPaymentId = booking.getMethodPayment().getId();
                    String methodPaymentName = booking.getMethodPayment().getMethodPaymentName();
                    String discountName = booking.getDiscountName();
                    Double discountPercent = booking.getDiscountPercent();
                    Integer statusBookingID = booking.getStatus().getId();
                    // Tạo BookingHistoryDTOs với thông tin đầy đủ
                    return new BookingHistoryDTOs(
                            (Integer) objects[0], // bk_id
                            (String) objects[1], // bkformat
                            (String) objects[2], // create_at
                            (String) objects[3], // start_at
                            (String) objects[4], // end_at
                            (String) objects[5], // fullname
                            (String) objects[6], // avatar
                            (Integer) objects[7], // statusBkID
                            (String) objects[8], // statusBkName
                            (Integer) objects[9], // iv_id
                            (Double) objects[10], // totalRoom
                            (Integer) objects[11], // fb_id
                            (String) objects[12], // content
                            (Integer) objects[13], // stars
                            (String) objects[14], // roomInfo
                            (String) objects[15], // image
                            (String) objects[16], // combinedServiceNames
                            (Double) objects[17], // combinedTotalServices
                            (Double) objects[18], // totalAmount
                            methodPaymentId, // Lấy từ Booking
                            methodPaymentName, // Lấy từ Booking
                            discountName, // Lấy từ Booking
                            discountPercent, // Lấy từ Booking
                            statusBookingID);
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/booking-by-room/{id}")
    public ResponseEntity<?> getBookingByRoom(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(bookingService.getBookingByRoom(id));
    }

    @PutMapping("/cancel-booking/{id}")
    public StatusResponseDto cancelBooking(@PathVariable("id") Integer id,
            @RequestParam("descriptions") String descriptions) {
        boolean flag = bookingService.cancelBooking(id, descriptions);
        if (flag) {
            return new StatusResponseDto("200", "success", "Hủy đặt phòng thành công");
        } else {
            return new StatusResponseDto("400", "error", "Hủy đặt phòng thất bại");
        }
    }

    @PostMapping("/booking-maintenance")
    public ResponseEntity<?> postMaintenanceSchedule(@Valid @RequestBody bookingModel bookingModels,
            @RequestParam("userName") String username) {
        System.out.println(bookingModels.getUserName());
        Map<String, String> response = new HashMap<String, String>();
        System.out.println("2");
        StatusResponseDto errorBookings = errorsServices.errorBooking(bookingModels);
        System.out.println("3");
        if (errorBookings != null) {
            return ResponseEntity.ok(errorBookings);
        }
        System.out.println("4");
        Boolean flag = bookingService.createMaintenanceSchedule(bookingModels, username);
        if (flag == true) {
            response = paramServices.messageSuccessApi(201, "success", "Tạo lịch thành công");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response = paramServices.messageSuccessApi(400, "error", "Tạo lịch thất bại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/room-usage")
    public ResponseEntity<?> getRoomUsage(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        if ("null".equals(startDate)) {
            startDate = null;
        }
        if ("null".equals(endDate)) {
            endDate = null;
        }

        // Gọi service để lấy thông tin về công suất phòng
        RoomUsageDTO roomUsage = bookingService.getRoomUsage(startDate, endDate);

        // Trả về kết quả
        return ResponseEntity.ok(roomUsage);
    }

    @PostMapping("/delete-booking")
    public ResponseEntity<?> deleteBooking(@Valid @RequestBody DeleteBookingModel bookingModels) {
        // nghia, hàm này được push sáng ngày 18 tháng 12 năm 2024
        Map<String, String> response = new HashMap<String, String>();
        StatusResponseDto statusResponseDto = errorsServices.errorDeleteBooking(bookingModels);
        if (statusResponseDto != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusResponseDto);
        }
        Boolean flag = bookingService.deleteBookings(bookingModels);
        if (!flag) {
            response = paramServices.messageSuccessApi(400, "error", "Hủy phòng thất bại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            response = paramServices.messageSuccessApi(201, "success", "Hủy phòng thành công");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
    }
}
