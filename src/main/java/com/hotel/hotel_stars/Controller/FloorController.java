package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.DTO.FloorDto;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Models.floorModel;
import com.hotel.hotel_stars.Service.FloorService;
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
@RequestMapping("api/floor")
public class FloorController {
    @Autowired
    FloorService flservice;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllFloors() {
        return ResponseEntity.ok(flservice.getAllFloors());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addFloor(@Valid @RequestBody floorModel flmodel) {
        try {
            FloorDto fldto = flservice.addFloor(flmodel);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Thêm thành công");
            response.put("status", "success");
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
            errorResponse.put("message", "" + ex.getMessage());
            errorResponse.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("update")
    public ResponseEntity<?> updateFloor(@Valid @RequestBody floorModel flmodel) {
        try {
            FloorDto updatedFl = flservice.updateFloor(flmodel);
            // tạo thông báo
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Cập nhật thành công");
            response.put("status", "success");
//            response.put("data", updatedFl);
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
            errorResponse.put("message", "" + ex.getMessage());
            errorResponse.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteAmenitiesTypeRoom(@PathVariable Integer id) {
        try {
            // Gọi phương thức trong service để xóa tài khoản
            flservice.deleteFloor(id);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Xóa thành công");
            response.put("status", "success");
            return ResponseEntity.ok(response);
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
            errorResponse.put("message", "Không thể xóa tầng này đang được sử dụng.");
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
}
