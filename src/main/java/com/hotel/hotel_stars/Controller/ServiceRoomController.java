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
            response.put("status", "success");
//            response.put("data", srdto);
            return ResponseEntity.ok(response); // Trả về phản hồi với mã 200
        } catch (CustomValidationException ex) {
            // Trả về lỗi xác thực với danh sách thông báo lỗi
            // Hiển thỉ lỗi 400, 500
            return ResponseEntity.badRequest().body(ex.getErrorMessages());
        } catch (RuntimeException ex) {
            // Trả về lỗi chung cho các lỗi không xác thực
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + ex.getMessage());
        }
    }

    @PutMapping("update-service-room/{id}")
    public ResponseEntity<?> updateServiceRoom(@PathVariable Integer id, @Valid @RequestBody serviceRoomModel srmodel) {
        try {
            ServiceRoomDto updatedServiceRoom = srservice.updateServiceRoom(id, srmodel);
            // tạo thông báo
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Cập nhật dịch vụ phòng thành công");
            response.put("status", "success");
//            response.put("data", updatedServiceRoom);
            return ResponseEntity.ok(response); // Trả về phản hồi với mã 200
        } catch (CustomValidationException ex) {
            // Trả về lỗi xác thực với danh sách thông báo lỗi
            return ResponseEntity.badRequest().body(ex.getErrorMessages());
        } catch (RuntimeException ex) {
            // Trả về lỗi chung cho các lỗi không xác thực
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + ex.getMessage());
        }
    }


    @DeleteMapping("delete-service-room/{id}")
    public ResponseEntity<?> deleteServiceRoom(@PathVariable Integer id) {
        try {
            // Gọi phương thức trong service để xóa tài khoản
            srservice.deleteServiceRoom(id);
            return ResponseEntity.ok("Dịch vụ phòng này đã được xóa thành công."); // Phản hồi thành công
        } catch (NoSuchElementException ex) {
            // Trả về lỗi nếu tài khoản không tồn tại
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dịch vụ phòng này không tồn tại.");
        } catch (RuntimeException ex) {
            // Trả về lỗi chung cho các lỗi không xác thực
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + ex.getMessage());
        }
    }
}