package com.hotel.hotel_stars.Controller;


import com.hotel.hotel_stars.DTO.StatusRoomDto;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Models.statusRoomModel;
import com.hotel.hotel_stars.Service.StatusRoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin("*")
@RequestMapping("api/status-room")
public class StatusRoomController {
    @Autowired
    StatusRoomService strservice;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllStatusRooms() {
        return ResponseEntity.ok(strservice.getAllStatusRooms());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addStatusRoom(@Valid @RequestBody statusRoomModel atrmodel) {
        try {
            StatusRoomDto strdto = strservice.addStatusRoom(atrmodel);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Thêm thành công");
            response.put("status", "success");
//      response.put("data", atrdto); // Uncomment if you want to return data
            return ResponseEntity.ok(response); // Trả về phản hồi với mã 200
        } catch (CustomValidationException ex) {
            // Trả về lỗi xác thực với mã lỗi 400
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 400);
            errorResponse.put("message", "Lỗi xác thực khi thêm vào");
            errorResponse.put("status", "error");
            errorResponse.put("errors", ex.getErrorMessages());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (RuntimeException ex) {
            // Trả về lỗi máy chủ nội bộ với mã lỗi 500
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Có lỗi xảy ra khi thêm vào: " + ex.getMessage());
            errorResponse.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateAmenitiesTypeRoom(@PathVariable Integer id, @Valid @RequestBody statusRoomModel strmodel) {
        try {
            StatusRoomDto updatedStr = strservice.updateStatusRoom(id, strmodel);
            // tạo thông báo
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Cập nhật thành công");
            response.put("status", "success");
//            response.put("data", updatedServiceRoom);
            return ResponseEntity.ok(response); // Trả về phản hồi với mã 200
        } catch (CustomValidationException ex) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 400);
            errorResponse.put("message", "Lỗi xác thực khi cập nhật");
            errorResponse.put("status", "error");
            errorResponse.put("errors", ex.getErrorMessages());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (RuntimeException ex) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Có lỗi xảy ra khi cập nhật vào: " + ex.getMessage());
            errorResponse.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteStatusRoom(@PathVariable Integer id) {
        try {
            // Gọi phương thức trong service để xóa tài khoản
            strservice.deleteStatusRoom(id);
            return ResponseEntity.ok("Đã xóa thành công."); // Phản hồi thành công
        } catch (NoSuchElementException ex) {
            // Trả về lỗi nếu tài khoản không tồn tại
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 404);
            errorResponse.put("message", "Tên bạn muốn xóa không tồn tại.");
            errorResponse.put("status", "error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (DataIntegrityViolationException ex) {
            // Xử lý lỗi xung đột khóa ngoại
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 409); // Mã lỗi cho xung đột
            errorResponse.put("message", "Không thể xóa vì có dữ liệu liên quan.");
            errorResponse.put("status", "error");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (CustomValidationException ex) {
            // Xử lý lỗi xác thực (nếu có)
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 400);
            errorResponse.put("message", "Lỗi xác thực khi xóa");
            errorResponse.put("status", "error");
            errorResponse.put("errors", ex.getErrorMessages());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (RuntimeException ex) {
            // Trả về lỗi chung cho các lỗi không xác thực
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Có lỗi xảy ra khi xóa: " + ex.getMessage());
            errorResponse.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("get-status-excluding/{id}")
    public ResponseEntity<?> getExcludingId(@PathVariable("id") Integer id){
    	return ResponseEntity.ok(strservice.getByExcludingId(id));
    }
}
