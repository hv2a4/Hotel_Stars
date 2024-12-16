package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.DTO.CustomerInformationDto;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Models.customerInformationModel;
import com.hotel.hotel_stars.Service.CustomerInformationService;
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
@RequestMapping("api/customer-info")
public class CustomerInfomationController {
    @Autowired
    CustomerInformationService customerInformationService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllCustomerInformation() {
        return ResponseEntity.ok(customerInformationService.getAllCustomerInformation());
    }

    // API thêm thông tin khách hàng
    @PostMapping("/add")
    public ResponseEntity<?> addCustomerInformation(@Valid @RequestBody customerInformationModel customerModel, @RequestParam("idBookingRoom") Integer idBookingRoom) {
        try {
            CustomerInformationDto customerDto = customerInformationService.addCustomerInformation(customerModel,idBookingRoom);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Thêm thành công");
            response.put("status", "success");
            return ResponseEntity.ok(response); // Trả về phản hồi với mã 200
        } catch (CustomValidationException ex) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 400);
            errorResponse.put("message", "Lỗi xác thực khi thêm vào");
            errorResponse.put("status", "error");
            errorResponse.put("errors", ex.getErrorMessages());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (RuntimeException ex) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Có lỗi xảy ra khi thêm vào: " + ex.getMessage());
            errorResponse.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // API sửa thông tin khách hàng
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCustomerInformation(@PathVariable Integer id, @Valid @RequestBody customerInformationModel customerModel, @RequestParam("bookingRoomId") Integer idBookingRoom) {
        try {
            CustomerInformationDto updatedCustomer = customerInformationService.updateCustomerInformation(id, customerModel,idBookingRoom);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Cập nhật thành công");
            response.put("status", "success");
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
            errorResponse.put("message", "Có lỗi xảy ra khi cập nhật: " + ex.getMessage());
            errorResponse.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // API xóa thông tin khách hàng
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCustomerInformation(@PathVariable Integer id) {
        try {
            customerInformationService.deleteCustomerInformation(id);
            return ResponseEntity.ok("Đã xóa thành công."); // Phản hồi thành công
        } catch (NoSuchElementException ex) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 404);
            errorResponse.put("message", "Khách hàng không tồn tại.");
            errorResponse.put("status", "error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (DataIntegrityViolationException ex) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 409); // Mã lỗi cho xung đột
            errorResponse.put("message", "Không thể xóa vì có dữ liệu liên quan.");
            errorResponse.put("status", "error");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (CustomValidationException ex) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 400);
            errorResponse.put("message", "Lỗi xác thực khi xóa");
            errorResponse.put("status", "error");
            errorResponse.put("errors", ex.getErrorMessages());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (RuntimeException ex) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Có lỗi xảy ra khi xóa: " + ex.getMessage());
            errorResponse.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
