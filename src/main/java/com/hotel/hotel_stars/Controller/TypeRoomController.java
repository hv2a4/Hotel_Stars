package com.hotel.hotel_stars.Controller;

import java.util.*;

import com.hotel.hotel_stars.Repository.TypeRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.hotel_stars.DTO.TypeRoomDto;
import com.hotel.hotel_stars.DTO.Select.BookingDetailDTO;
import com.hotel.hotel_stars.DTO.Select.PaginatedResponse;
import com.hotel.hotel_stars.DTO.Select.RoomTypeDetail;
import com.hotel.hotel_stars.DTO.selectDTO.FindTypeRoomDto;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Models.TypeRoomAmenitiesTypeRoomModel;
import com.hotel.hotel_stars.Models.typeRoomModel;
import com.hotel.hotel_stars.Service.AccountService;
import com.hotel.hotel_stars.Service.BookingService;
import com.hotel.hotel_stars.Service.TypeRoomService;
import com.hotel.hotel_stars.utils.paramService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin("*")
@RequestMapping("api/type-room")
public class TypeRoomController {
    @Autowired
    TypeRoomService trservice;
    @Autowired
    private AccountService accountService;

    @Autowired
    BookingService bookingService;

    @Autowired
    paramService paramService;
    @Autowired
    TypeRoomRepository typeRoomRepository;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllTypeRooms() {
        return ResponseEntity.ok(trservice.getAllTypeRooms());
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<BookingDetailDTO>> getBookingDetails(@PathVariable Integer accountId) {
        return ResponseEntity.ok(bookingService.getBookingDetailsByAccountId(accountId));
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
            if (ex.getMessage().contains("409")) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 409);
                response.put("message", "Tên loại phòng này đã tồn tại.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // Mã 409
            }
            if (ex.getMessage().contains("422")) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 422);
                response.put("message", "Vui lòng chọn ít nhất một hình ảnh!");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // Mã 422
            }
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "Có lỗi xảy ra: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Mã 500
        }
    }

    @PutMapping("update")
    public ResponseEntity<?> updateTypeRoom(@Valid @RequestBody typeRoomModel trmodel) {
        try {
            TypeRoomDto updatedTypeRoom = trservice.updateTypeRoom(trmodel);
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
            response.put("message", "Loại phòng này đã được xóa thành công.");
            return ResponseEntity.ok(response); // Mã 200

        } catch (NoSuchElementException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 404);
            response.put("message", "Loại phòng này không tồn tại.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Mã 404

        } catch (DataIntegrityViolationException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 409);
            response.put("message", "Không thể xóa loại phòng này đang được sử dụng!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // Mã 409: Conflict

        } catch (RuntimeException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "Có lỗi xảy ra: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Mã 500
        }
    }

    @GetMapping("/top3")
    public ResponseEntity<?> getTop3TypeRooms() {
        return ResponseEntity.ok(trservice.getTypeRooms());
    }

    @GetMapping("/find-amenities-type-rom/{idTypeRoom}")
    public ResponseEntity<List<TypeRoomAmenitiesTypeRoomModel>> getTypeRoomAmenitiesTypeRoom(
            @PathVariable Integer idTypeRoom) {
        return ResponseEntity.ok(trservice.getTypeRoomAmenitiesTypeRoom(idTypeRoom));
    }

    @GetMapping("/find-by-id")
    public ResponseEntity<TypeRoomDto> getTypeRoomById(@RequestParam Integer id) {
        TypeRoomDto typeRoomDto = trservice.getTypeRoomsById(id);
        return ResponseEntity.ok(typeRoomDto); // Trả về ResponseEntity với dữ liệu và mã trạng thái OK (200)
    }

    // @GetMapping("/find-type-room")
    // public ResponseEntity<?> findTypeRoom(
    // @RequestParam String startDate,
    // @RequestParam String endDate,
    // @RequestParam Integer guestLimit) {
    //
    // return ResponseEntity.ok(trservice.getRoom(startDate, endDate, guestLimit));
    // // Trả về ResponseEntity với dữ
    // }

    @GetMapping("/find-type-room")
    public ResponseEntity<?> findTypeRoom(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam Integer guestLimit,
            @RequestParam (defaultValue = "0", required = false) Integer typeRoomID,
            @RequestParam(defaultValue = "1") Integer page, // Mặc định là trang 1
            @RequestParam(defaultValue = "10") Integer size // Mặc định là 10 bản ghi/trang
    ) {
        // Tạo Pageable từ page và size
        Pageable pageable = PageRequest.of(page - 1, size);
        Integer processedTypeRoomID = Optional.ofNullable(typeRoomID)
                .filter(id -> id != 0)
                .orElse(null);
        boolean isValidTypeRoomID = typeRoomRepository.existsById(typeRoomID);
        if (!isValidTypeRoomID) {
            processedTypeRoomID = null; // Gán NULL nếu typeRoomID không hợp lệ
        }
        // Fetch dữ liệu phòng với phân trang từ service
        Page<FindTypeRoomDto> rooms = trservice.getRoom(startDate, endDate, guestLimit, processedTypeRoomID,pageable);

        // Lấy tổng số phòng và tổng số trang từ Page object
        long totalItems = rooms.getTotalElements();
        int totalPages = rooms.getTotalPages();

        // Tạo đối tượng response chứa dữ liệu phân trang
        PaginatedResponse<FindTypeRoomDto> response = new PaginatedResponse<>(rooms.getContent(), totalItems, totalPages, page, size);

        return ResponseEntity.ok(response);
    }
        
    @GetMapping("/detail-type-room")
    public ResponseEntity<?> getTypeRoomDetail(@RequestParam Integer id) {
        List<RoomTypeDetail> typeRoomDto = trservice.getRoomTypeDetailById(id);
        return ResponseEntity.ok(typeRoomDto);
    }

    @GetMapping("/accountId/{id}")
    public ResponseEntity<?> getBookingByAccount(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(bookingService.getListByAccountId(id));
    }
    @GetMapping("/find-all-type-room")
    public ResponseEntity<?> findAllTypeRoomGroupRoom(
            @RequestParam(defaultValue = "1") Integer page, // Mặc định là trang 1
            @RequestParam(defaultValue = "10") Integer size // Mặc định là 10 bản ghi/trang
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<FindTypeRoomDto> rooms = trservice.getTypeRoomGroupRoom(pageable);

        // Lấy tổng số phòng và tổng số trang từ Page object
        long totalItems = rooms.getTotalElements();
        int totalPages = rooms.getTotalPages();

        // Tạo đối tượng response chứa dữ liệu phân trang
        PaginatedResponse<FindTypeRoomDto> response = new PaginatedResponse<>(rooms.getContent(), totalItems, totalPages, page, size);

        return ResponseEntity.ok(response);
    }
}
