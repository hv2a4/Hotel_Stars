package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.Models.ServiceHotelModel;
import com.hotel.hotel_stars.Service.ServiceHotelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("api/service-hotel")
public class ServiceController {
    @Autowired
    ServiceHotelService serviceHotelService;

    @RequestMapping("getAll")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(serviceHotelService.getAllServiceHotels());
    }

    @PostMapping("post-data-service-hotel")
    public ResponseEntity<StatusResponseDto> postDataServiceHotel(@Valid @RequestBody ServiceHotelModel serviceHotelModel) {
        StatusResponseDto response = serviceHotelService.addServiceHotel(serviceHotelModel);
        return buildResponse(response);
    }

    @PutMapping("update-data-service-hotel")
    public ResponseEntity<StatusResponseDto> updateDataServiceHotel(@Valid @RequestBody ServiceHotelModel serviceHotelModel) {
        StatusResponseDto response = serviceHotelService.updateServiceHotel(serviceHotelModel);
        return buildResponse(response);
    }

    @DeleteMapping("delete-data-service-hotel/{id}")
    public ResponseEntity<StatusResponseDto> deleteDataServiceHotel(@PathVariable("id") Integer id) {
        StatusResponseDto response = serviceHotelService.deleteById(id);

        // Dựa vào mã phản hồi trong `StatusResponseDto`, trả về mã trạng thái HTTP phù hợp
        switch (response.getCode()) {
            case "200":
                return ResponseEntity.ok(response);  // Trả về HTTP 200 nếu xóa thành công

            case "404":
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);  // HTTP 404 nếu không tìm thấy

            case "400":
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // HTTP 400 cho lỗi dữ liệu

            case "500":
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);  // HTTP 500 cho các lỗi không xác định
        }
    }

    @GetMapping("/service-hotel/{id}")
    public ResponseEntity<?> getServiceHotelById(@PathVariable Integer id) {
        return ResponseEntity.ok(serviceHotelService.getAllServiceHotelsByHotelId(id));
    }

    private ResponseEntity<StatusResponseDto> buildResponse(StatusResponseDto response) {
        switch (response.getCode()) {
            case "200":
                return ResponseEntity.ok(response);  // HTTP 200 OK cho phản hồi thành công
            case "404":
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);  // HTTP 404 Not Found cho lỗi không tìm thấy
            case "400":
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // HTTP 400 Bad Request cho lỗi dữ liệu
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);  // HTTP 500 Internal Server Error cho lỗi không xác định
        }
    }
}
