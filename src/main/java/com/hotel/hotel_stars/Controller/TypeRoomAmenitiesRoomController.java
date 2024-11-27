package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.DTO.TypeRoomAmenitiesTypeRoomDto;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Models.TypeRoomAmenitiesTypeRoomModel;
import com.hotel.hotel_stars.Service.TypeRoomAmenitiesTypeRoomService;
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
@RequestMapping("api/type-room-amenities-type-room")
public class TypeRoomAmenitiesRoomController {
    @Autowired
    TypeRoomAmenitiesTypeRoomService trAtrService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllTrAtr() {
        return ResponseEntity.ok(trAtrService.getAllTrAtr());
    }

    @GetMapping("/getByTypeRoomId/{typeRoomId}")
    public ResponseEntity<?> getAmenitiesByTypeRoomId(@PathVariable Integer typeRoomId) {
        return ResponseEntity.ok(trAtrService.getAmenitiesByTypeRoomId(typeRoomId));
//        try {
//            List<TypeRoomAmenitiesTypeRoomDto> amenities = trAtrService.getAmenitiesByTypeRoomId(typeRoomId);
//            Map<String, Object> response = new HashMap<>();
//            response.put("code", 200);
//            response.put("data", amenities);
//            response.put("message", "Lấy danh sách tiện nghi thành công");
//            response.put("status", "success");
//            return ResponseEntity.ok(response);
//        } catch (RuntimeException ex) {
//            Map<String, Object> response = new HashMap<>();
//            response.put("code", 500);
//            response.put("message", ex.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
    }

//    @PostMapping("/add")
//    public ResponseEntity<?> addTypeRoom(@Valid @RequestBody TypeRoomAmenitiesTypeRoomModel tr_atr_model) {
//        try {
//            TypeRoomAmenitiesTypeRoomDto tr_atr_dto = trAtrService.addTypeRoomAmenitiesTypeRoom(tr_atr_model);
//            Map<String, Object> response = new HashMap<>();
//            response.put("code", 200);
//            response.put("message", "Thêm thành công");
//            response.put("status", "success");
//            return ResponseEntity.ok(response); // Trả về phản hồi với mã 200 và thông tin trong response
//        } catch (CustomValidationException ex) {
//            // Trả về lỗi xác thực với danh sách thông báo lỗi
//            Map<String, Object> response = new HashMap<>();
//            response.put("code", 400);
//            response.put("message", "Lỗi xác thực");
//            response.put("errors", ex.getErrorMessages());
//            return ResponseEntity.badRequest().body(response); // Mã 400
//        } catch (RuntimeException ex) {
//            Map<String, Object> response = new HashMap<>();
//            response.put("status", 500);
//            response.put("message", ex.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Mã 500
//        }
//    }

//    @PutMapping("/update/{id}")
//    public ResponseEntity<?> updateTypeRoomAmenitiesTypeRoom(
//            @PathVariable Integer id,
//            @Valid @RequestBody TypeRoomAmenitiesTypeRoomModel tr_atr_model) {
//        try {
//            TypeRoomAmenitiesTypeRoomDto tr_atr_dto = trAtrService.updateTypeRoomAmenitiesTypeRoom(id, tr_atr_model);
//            Map<String, Object> response = new HashMap<>();
//            response.put("code", 200);
//            response.put("message", "Cập nhật thành công");
//            response.put("status", "success");
//            return ResponseEntity.ok(response);
//        } catch (CustomValidationException ex) {
//            Map<String, Object> response = new HashMap<>();
//            response.put("code", 400);
//            response.put("message", "Lỗi xác thực");
//            response.put("errors", ex.getErrorMessages());
//            return ResponseEntity.badRequest().body(response);
//        } catch (NoSuchElementException ex) {
//            Map<String, Object> response = new HashMap<>();
//            response.put("code", 404);
//            response.put("message", "Dữ liệu không tồn tại.");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//        } catch (RuntimeException ex) {
//            Map<String, Object> response = new HashMap<>();
//            response.put("code", 500);
//            response.put("message", "Có lỗi xảy ra: " + ex.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTypeRoomAmenitiesTypeRoom(@PathVariable Integer id) {
        try {
            trAtrService.deleteTypeRoomAmenitiesTypeRoom(id);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Xóa thành công");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 404);
            response.put("message", "Dữ liệu không tồn tại.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (RuntimeException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "Có lỗi xảy ra khi xóa: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
