package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.DTO.AccountDto;
import com.hotel.hotel_stars.DTO.ServiceRoomDto;
import com.hotel.hotel_stars.Entity.ServiceRoom;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Models.accountModel;
import com.hotel.hotel_stars.Service.ServiceRoomService;
import com.hotel.hotel_stars.Models.serviceRoomModel;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin("*")
@RequestMapping("api/service-room")
public class ServiceRoomController {
	@Autowired
	ServiceRoomService srservice;

	@GetMapping("/getAll")
	public ResponseEntity<?> getAllServiceRooms() {
		return ResponseEntity.ok(srservice.getAllServiceRooms());
	}

	@PostMapping("/add-service-room")
	public ResponseEntity<?> addServiceRoom(@Valid @RequestBody serviceRoomModel srmodel) {
		try {
			ServiceRoomDto srdto = srservice.addServiceRoom(srmodel);
			Map<String, Object> response = new HashMap<>();
			response.put("code", 200);
			response.put("message", "Thêm dịch vụ phòng thành công");
			response.put("status", "success");// Trả về dữ liệu của dịch vụ phòng đã cập nhật
			return ResponseEntity.ok(response); // Trả về phản hồi với mã 200
		} catch (CustomValidationException ex) {
			Map<String, Object> response = new HashMap<>();
			response.put("code", 400);
			response.put("message", "Lỗi xác thực");
			response.put("status", "error");
			response.put("errors", ex.getErrorMessages()); // Các lỗi xác thực
			return ResponseEntity.badRequest().body(response); // Mã 400
		} catch (NoSuchElementException ex) {
			Map<String, Object> response = new HashMap<>();
			response.put("code", 404);
			response.put("message", "Dịch vụ phòng không tồn tại");
			response.put("status", "error");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Mã 404
		} catch (RuntimeException ex) {
			Map<String, Object> response = new HashMap<>();
			response.put("code", 500);
			response.put("message", "Có lỗi xảy ra: " + ex.getMessage());
			response.put("status", "error");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Mã 500
		}
	}

	@PutMapping("update-service-room/{id}")
	public ResponseEntity<?> updateServiceRoom(@PathVariable Integer id, @Valid @RequestBody serviceRoomModel srmodel) {
		try {
			ServiceRoomDto updatedServiceRoom = srservice.updateServiceRoom(id, srmodel);
			Map<String, Object> response = new HashMap<>();
			response.put("code", 200);
			response.put("message", "Cập nhật dịch vụ phòng thành công");
			response.put("status", "success");
			response.put("data", updatedServiceRoom); // Trả về dữ liệu của dịch vụ phòng đã cập nhật
			return ResponseEntity.ok(response); // Trả về phản hồi với mã 200
		} catch (CustomValidationException ex) {
			Map<String, Object> response = new HashMap<>();
			response.put("code", 400);
			response.put("message", "Lỗi xác thực");
			response.put("status", "error");
			response.put("errors", ex.getErrorMessages()); // Các lỗi xác thực
			return ResponseEntity.badRequest().body(response); // Mã 400
		} catch (NoSuchElementException ex) {
			Map<String, Object> response = new HashMap<>();
			response.put("code", 404);
			response.put("message", "Dịch vụ phòng không tồn tại");
			response.put("status", "error");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Mã 404
		} catch (RuntimeException ex) {
			Map<String, Object> response = new HashMap<>();
			response.put("code", 500);
			response.put("message", "Có lỗi xảy ra: " + ex.getMessage());
			response.put("status", "error");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Mã 500
		}
	}

	@DeleteMapping("delete-service-room/{id}")
	public ResponseEntity<?> deleteServiceRoom(@PathVariable Integer id) {
		try {
			srservice.deleteServiceRoom(id);
			Map<String, Object> response = new HashMap<>();
			response.put("code", 200);
			response.put("message", "Dịch vụ phòng này đã được xóa thành công.");
			return ResponseEntity.ok(response); // Trả về phản hồi thành công với mã 200
		} catch (NoSuchElementException ex) {
			Map<String, Object> response = new HashMap<>();
			response.put("code", 404);
			response.put("message", "Dịch vụ phòng này không tồn tại.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Mã 404
		} catch (DataIntegrityViolationException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("code", "400");
			response.put("message", "Không thể xóa vì có dữ liệu liên quan ");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} catch (RuntimeException ex) {
			Map<String, Object> response = new HashMap<>();
			response.put("code", 500);
			response.put("message", "Có lỗi xảy ra: " + ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Mã 500
		}
	}
	
	@GetMapping("/booking-room")
	public ResponseEntity<?> BookingRoomServiceRoomService(@RequestParam("idBookingRoom") Integer idBookingRoom){
		return ResponseEntity.ok(srservice.getServiceRoomsByBookingRoomId(idBookingRoom));
	}

}