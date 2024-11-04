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
    public ResponseEntity<?> addServiceRoom(@Valid @RequestBody typeRoomModel trmodel) {
        try {
            TypeRoomDto trdto = trservice.addTypeRoom(trmodel);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Thêm loại phòng thành công");
            response.put("status", "success");
//            response.put("data", srdto);
            return ResponseEntity.ok(response); // Trả về phản hồi với mã 200
        } catch (CustomValidationException ex) {
            // Trả về lỗi xác thực với danh sách thông báo lỗi
            return ResponseEntity.badRequest().body(ex.getErrorMessages());
        } catch (RuntimeException ex) {
            // Trả về lỗi chung cho các lỗi không xác thực
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + ex.getMessage());
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateServiceRoom(@PathVariable Integer id, @Valid @RequestBody typeRoomModel trmodel) {
        try {
            TypeRoomDto updatedTypeRoom = trservice.updateTypeRoom(id, trmodel);
            // tạo thông báo
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Cập nhật dịch vụ phòng thành công");
            response.put("status", "success");
//            response.put("data", updatedTypeRoom);
            return ResponseEntity.ok(response); // Trả về phản hồi với mã 200
        } catch (CustomValidationException ex) {
            // Trả về lỗi xác thực với danh sách thông báo lỗi
            return ResponseEntity.badRequest().body(ex.getErrorMessages());
        } catch (RuntimeException ex) {
            // Trả về lỗi chung cho các lỗi không xác thực
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + ex.getMessage());
        }
    }


    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteTypeRoom(@PathVariable Integer id) {
        try {
            // Gọi phương thức trong service để xóa tài khoản
            trservice.deleteServiceRoom(id);
            return ResponseEntity.ok("Dịch vụ phòng này đã được xóa thành công."); // Phản hồi thành công
        } catch (NoSuchElementException ex) {
            // Trả về lỗi nếu tài khoản không tồn tại
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dịch vụ phòng này không tồn tại.");
        } catch (RuntimeException ex) {
            // Trả về lỗi chung cho các lỗi không xác thực
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + ex.getMessage());
        }
    }

    @GetMapping("/top3")
    public List<TypeRoomBookingCountDto> getTop3TypeRooms() {
        return trservice.getTop3TypeRooms();
    }
}
