package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.Config.JwtService;
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
import com.hotel.hotel_stars.utils.paramService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;

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
	public ResponseEntity<?> updateStatus(@PathVariable("id") Integer idBooking, @PathVariable("idStatus") Integer idStatus, @RequestBody bookingModelNew bookingModel) {
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


	// khoi

	@GetMapping("/account/payment-info/{id}")
	public ResponseEntity<List<PaymentInfoDTO>> getBookingPaymentInfo(@PathVariable Integer id) {
		return ResponseEntity.ok(bookingService.getPaymentInfoByAccountId(id));
	}

	@PostMapping("/sendBooking")
	public ResponseEntity<?> postBooking(@Valid @RequestBody bookingModel bookingModels) {
		Map<String, String> response = new HashMap<String, String>();
		errorsServices.errorBooking(bookingModels);
		Boolean flag = bookingService.sendBookingEmail(bookingModels);
		if (flag == true) {
			response = paramServices.messageSuccessApi(201, "success",
					"Đặt phòng thành công, vui lòng vào email để xác nhận");
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} else {
			response = paramServices.messageSuccessApi(400, "error", "Đặt phòng thất bại");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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
			booking.setStatus(statusBooking.get());

			try {
				bookingRepository.save(booking);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return ResponseEntity.ok(paramServices.confirmBookings(booking, formattedAmount));
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