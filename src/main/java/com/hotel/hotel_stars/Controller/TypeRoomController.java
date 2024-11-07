package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.DTO.Select.TypeRoomBookingCountDto;
import com.hotel.hotel_stars.DTO.TypeRoomDto;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Models.typeRoomModel;
import com.hotel.hotel_stars.Service.TypeRoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin("*")
@RequestMapping("api/type-room")
public class TypeRoomController {
    @Autowired
    TypeRoomService trservice;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllTypeRooms() {
        return ResponseEntity.ok(trservice.getAllTypeRooms());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTypeRoom(@Valid @RequestBody typeRoomModel trmodel) {
        try {
            TypeRoomDto trdto = trservice.addTypeRoom(trmodel);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Thêm loại phòng thành công");
            response.put("status", "success");
            return ResponseEntity.ok(response); // Trả về phản hồi với mã 200 và thông tin trong response
        } catch (CustomValidationException ex) {
            // Trả về lỗi xác thực với danh sách thông báo lỗi
            Map<String, Object> response = new HashMap<>();
            response.put("code", 400);
            response.put("message", "Lỗi xác thực");
            response.put("errors", ex.getErrorMessages());
            return ResponseEntity.badRequest().body(response); // Mã 400
        } catch (RuntimeException ex) {
            if (ex.getMessage().contains("Tên loại phòng này đã tồn tại")) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 409);
                response.put("message", "Tên loại phòng này đã tồn tại.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // Mã 409
            }
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "Có lỗi xảy ra: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Mã 500
        }
    }


    @PutMapping("update/{id}")
    public ResponseEntity<?> updateTypeRoom(@PathVariable Integer id, @Valid @RequestBody typeRoomModel trmodel) {
        try {
            TypeRoomDto updatedTypeRoom = trservice.updateTypeRoom(id, trmodel);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Cập nhật dịch vụ phòng thành công");
            response.put("status", "success");
            return ResponseEntity.ok(response); // Trả về phản hồi với mã 200 và thông tin trong response
        } catch (CustomValidationException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 400);
            response.put("message", "Lỗi xác thực");
            response.put("errors", ex.getErrorMessages());
            return ResponseEntity.badRequest().body(response); // Mã 400
        } catch (NoSuchElementException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 404);
            response.put("message", "Loại phòng không tồn tại.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Mã 404
        } catch (RuntimeException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "Có lỗi xảy ra: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Mã 500
        }
    }


    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteTypeRoom(@PathVariable Integer id) {
        try {
            trservice.deleteTypeRoom(id);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Dịch vụ phòng này đã được xóa thành công.");
            return ResponseEntity.ok(response); // Mã 200
        } catch (NoSuchElementException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 404);
            response.put("message", "Dịch vụ phòng này không tồn tại.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Mã 404
        } catch (RuntimeException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "Có lỗi xảy ra: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Mã 500
        }
    }


    @GetMapping("/top3")
    public List<TypeRoomBookingCountDto> getTop3TypeRooms() {
        return trservice.getTop3TypeRooms();
    }
}
