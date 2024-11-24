package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.DTO.AmenitiesHotelDto;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Models.amenitiesHotelModel;
import com.hotel.hotel_stars.Service.AmenitiesHotelService;
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
@RequestMapping("api/amenitiesHotel")
public class AmenitiesHotelController {
    @Autowired
    AmenitiesHotelService ahservice;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllAmenitiesHotels() {
        return ResponseEntity.ok(ahservice.getAllAmenitiesHotels());
    }

    @GetMapping("/check-exist")
    public ResponseEntity<Map<String, Object>> checkExist(@RequestParam("name") String amenitiesTypeRoomName) {
        boolean exists = ahservice.checkIfExistsByName(amenitiesTypeRoomName);
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getAmenitiesHotelById(@PathVariable Integer id) {
        try {
            AmenitiesHotelDto atrDto = ahservice.getAmenitiesHotelById(id);
            Map<String, Object> response = new HashMap<>();
//            response.put("code", 200);
//            response.put("message", "Lấy thông tin thành công");
//            response.put("status", "success");
            response.put("data", atrDto);
            return ResponseEntity.ok(response); // Trả về phản hồi với mã 200 và dữ liệu
        } catch (NoSuchElementException ex) {
            // Xử lý lỗi nếu không tìm thấy dịch vụ phòng
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 404);
            errorResponse.put("message", "Dịch vụ khách sạn không tồn tại.");
            errorResponse.put("status", "error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (RuntimeException ex) {
            // Xử lý lỗi chung cho các lỗi không xác định
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Có lỗi xảy ra khi lấy tiện nghi khách sạn: " + ex.getMessage());
            errorResponse.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAmenitiesHotel(@Valid @RequestBody amenitiesHotelModel ahmodel) {
        try {
            AmenitiesHotelDto ahdto = ahservice.addAmenitiesHotel(ahmodel);
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
            errorResponse.put("message", "" + ex.getMessage());
            errorResponse.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("update")
    public ResponseEntity<?> updateAmenitiesHotel(@Valid @RequestBody amenitiesHotelModel ahmodel) {
        try {
            AmenitiesHotelDto updatedAh = ahservice.updateAmenitiesHotel(ahmodel);
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
            errorResponse.put("message", "" + ex.getMessage());
            errorResponse.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteAmenitiesHotel(@PathVariable Integer id) {
        try {
            // Gọi phương thức trong service để xóa tài khoản
            ahservice.deleteTypeRoom(id);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Xóa tiện nghi thành công");
            response.put("status", "success");
            return ResponseEntity.ok(response); // Phản hồi thành công
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
            errorResponse.put("message", "Không thể xóa vì tiện nghi đang được sử dụng.");
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
