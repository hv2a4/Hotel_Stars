package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.DTO.DiscountDto;
import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.Entity.Discount;
import com.hotel.hotel_stars.Entity.TypeRoom;
import com.hotel.hotel_stars.Models.DiscountModel;
import com.hotel.hotel_stars.Repository.DiscountRepository;
import com.hotel.hotel_stars.Repository.TypeRoomRepository;
import com.hotel.hotel_stars.Service.DiscountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.NoSuchElementException;

@RequestMapping("api/discount")
@RestController
@CrossOrigin("*")
public class DiscountController {
    @Autowired
    DiscountService discountService;
    @Autowired
    private TypeRoomRepository typeRoomRepository;
    @Autowired
    private DiscountRepository discountRepository;

    @GetMapping("getAll")
    public ResponseEntity<?> getAllDiscount() {
        return ResponseEntity.ok(discountService.getAllDiscountDtos());
    }
    @GetMapping("get-discount-account")
    public ResponseEntity<?> getDiscountByAccount(@RequestParam String username) {
        return ResponseEntity.ok(discountService.getDiscountByAccount(username));
    }
    @GetMapping("getAllDiscountTR")
    public ResponseEntity<?> getDiscountByTR(@RequestParam(required = false) Integer typeRoomId) {
        return ResponseEntity.ok(discountService.getDiscountByTypeRoom(typeRoomId));
    }

    @GetMapping("get-by-id/{id}")
    public ResponseEntity<?> getDiscountById(@PathVariable Integer id) {
        return ResponseEntity.ok(discountService.findDiscountDtoById(id));
    }

    @PostMapping("post-discount")
    public ResponseEntity<StatusResponseDto> postDiscount(@RequestBody DiscountModel discountModel) {
        StatusResponseDto response = discountService.saveDiscountDto(discountModel);

        HttpStatus status;
        switch (response.getCode()) {
            case "200":
                status = HttpStatus.OK;
                break;
            case "400":
                status = HttpStatus.BAD_REQUEST;
                break;
            case "404":
                status = HttpStatus.NOT_FOUND;
                break;
            default:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
        }
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping("update-discount")
    public ResponseEntity<StatusResponseDto> updateDiscount(@Valid @RequestBody DiscountModel discountModel) {
        try {
            // Gọi dịch vụ để cập nhật giảm giá
            DiscountDto result = discountService.updateDiscountDto(discountModel);

            // Tạo phản hồi thành công
            StatusResponseDto response = new StatusResponseDto("200", "SUCCESS", "Cập nhật giảm giá thành công.");
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            // Xử lý lỗi không tìm thấy đối tượng
            StatusResponseDto response = new StatusResponseDto("404", "FAILURE", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalArgumentException e) {
            // Xử lý lỗi hợp lệ của dữ liệu
            StatusResponseDto response = new StatusResponseDto("400", "FAILURE", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            // Xử lý lỗi chung
            StatusResponseDto response = new StatusResponseDto("500", "FAILURE", "Cập nhật giảm giá thất bại.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/delete-discount/{id}")
    public ResponseEntity<StatusResponseDto> deleteDiscount(@PathVariable Integer id) {
        StatusResponseDto response = discountService.deletById(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(Integer.parseInt(response.getCode())));
    }

}
