package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.Service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin("*")
public class ReservationController {
    @Autowired
    BookingService bookingService;

    @GetMapping("getAll")
    public ResponseEntity<?> getAllReservations() {
        return ResponseEntity.ok(bookingService.getAllReservationInfoDTO());
    }

    @GetMapping("selectBookingById")
    public ResponseEntity<?> selectBookingById(@RequestParam Integer bookingId, @RequestParam String roomName) {
        return ResponseEntity.ok(bookingService.mapToCustomerReservation(bookingId, roomName));
    }

    @PutMapping("statusBooking")
    public ResponseEntity<StatusResponseDto> updateStatus(
            @RequestParam Integer bookingId) {

        // Gọi service và nhận kết quả trả về
        StatusResponseDto response = bookingService.updateBookingStatus(bookingId);

        // Chọn mã trạng thái HTTP phù hợp dựa trên mã trong response
        if ("200".equals(response.getCode())) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if ("404".equals(response.getCode())) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
