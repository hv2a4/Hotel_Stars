package com.hotel.hotel_stars.Controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hotel.hotel_stars.DTO.Select.RoomOccupancyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.hotel.hotel_stars.DTO.RoomDto;
import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.DTO.Select.PaginatedResponseDto;
import com.hotel.hotel_stars.DTO.Select.RoomAvailabilityInfo;
import com.hotel.hotel_stars.Models.RoomModel;
import com.hotel.hotel_stars.Service.RoomService;
import com.hotel.hotel_stars.utils.paramService;

@RestController
@RequestMapping("api/room")
@CrossOrigin("*")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    paramService paramServices;

    @GetMapping("/getCountRoom")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(roomService.displayCounts());
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(roomService.getById(id));
    }

    @PostMapping("post-room")
    public ResponseEntity<StatusResponseDto> postRoom(@RequestBody RoomModel roomModel) {
        StatusResponseDto response = roomService.PostRoom(roomModel);

        // Thiết lập HTTP status dựa trên mã phản hồi
        HttpStatus status;
        switch (response.getCode()) {
            case "400":
                status = HttpStatus.BAD_REQUEST;
                break;
            case "409":
                status = HttpStatus.CONFLICT; // 409 Conflict
                break;
            case "500":
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
            default:
                status = HttpStatus.OK;
        }

        return ResponseEntity.status(status).body(response);
    }


    @PutMapping("put-room")
    public ResponseEntity<StatusResponseDto> putRoom(@RequestBody RoomModel roomModel) {
        StatusResponseDto response = roomService.PutRoom(roomModel);

        // Set response status based on the response code
        HttpStatus status = HttpStatus.OK; // Default to 200 OK
        switch (response.getCode()) {
            case "400":
                status = HttpStatus.BAD_REQUEST; // 400 Bad Request
                break;
            case "409":
                status = HttpStatus.CONFLICT; // 409 Conflict
                break;
            case "500":
                status = HttpStatus.INTERNAL_SERVER_ERROR; // 500 Internal Server Error
                break;
        }

        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<StatusResponseDto> deleteRoom(@PathVariable Integer id) {
        StatusResponseDto response = roomService.deleteById(id);

        if ("200".equals(response.getCode())) {
            return ResponseEntity.ok(response);  // Trả về mã 200 nếu xóa thành công
        } else if ("404".equals(response.getCode())) {
            return ResponseEntity.status(404).body(response);  // Trả về mã 404 nếu không tìm thấy phòng
        } else if ("409".equals(response.getCode())) {
            return ResponseEntity.status(409).body(response);  // Trả về mã 409 nếu có lỗi khóa ngoại
        } else {
            return ResponseEntity.status(500).body(response);  // Trả về mã 500 cho lỗi khác
        }
    }

    @GetMapping("/FloorById/{id}")
    public ResponseEntity<?> getByFloorId(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(roomService.getByFloorId(id));
    }

    @PutMapping("/update-active")
    public ResponseEntity<StatusResponseDto> updateActiveRoom(@RequestBody RoomModel model) {
        StatusResponseDto response = roomService.updateActiveRoom(model);
        HttpStatus status = HttpStatus.OK; // Default to 200 OK
        switch (response.getCode()) {
            case "400":
                status = HttpStatus.BAD_REQUEST; // 400 Bad Request
                break;
            case "500":
                status = HttpStatus.INTERNAL_SERVER_ERROR; // 500 Internal Server Error
                break;
        }
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("list-room-filter")
    public ResponseEntity<?> getRoomFilter(Pageable pageable) {
        // Lấy phân trang từ service
        Page<RoomAvailabilityInfo> roomPage = roomService.getAvailableRooms(pageable);

        // Tạo một bản đồ để trả về thông tin phân trang và danh sách
        Map<String, Object> response = new HashMap<>();
        response.put("rooms", roomPage.getContent()); // Danh sách các phòng
        response.put("totalItems", roomPage.getTotalElements()); // Tổng số mục
        response.put("totalPages", roomPage.getTotalPages()); // Tổng số trang
        response.put("currentPage", roomPage.getNumber()); // Trang hiện tại
        response.put("pageSize", roomPage.getSize()); // Kích thước trang

        return ResponseEntity.ok(response);
    }

    @GetMapping("/details")
    public ResponseEntity<?> getRoomDetails(@RequestParam Integer roomId) {
        return ResponseEntity.ok(roomService.getRoomDetailsByRoomId(roomId));
    }

    @GetMapping
    public ResponseEntity<PaginatedResponseDto<RoomDto>> getAllRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer guestLimit
    ) {
        LocalDate startLocalDate = startDate != null ? roomService.stringToLocalDate(startDate) : null;
        LocalDate endLocalDate = endDate != null ? roomService.stringToLocalDate(endDate) : null;

        return ResponseEntity.ok(roomService.getAll(page, size, sortBy, startLocalDate, endLocalDate, guestLimit));
    }


    @GetMapping("list-room")
    public ResponseEntity<?> getListRoomBookingId(@RequestParam List<Integer> roomId) {
        return ResponseEntity.ok(roomService.getRoomInBookingId(roomId));
    }

    @GetMapping("list-room-id")
    public ResponseEntity<?> getListRoomId(@RequestParam Integer roomId) {
        return ResponseEntity.ok(roomService.getListById(roomId));
    }

    @GetMapping("/room-occupancy")
    public ResponseEntity<?> getRoomOccupancy(@RequestParam(required = false) String startDate,
                                              @RequestParam(required = false) String endDate) {
        if ("null".equals(startDate)) {
            startDate = null;
        }
        if ("null".equals(endDate)) {
            endDate = null;
        }
        // Gọi service để lấy thông tin công suất phòng
        RoomOccupancyDTO result = roomService.getRoomOccupancyDTO(startDate, endDate);

        // Trả về kết quả
        return ResponseEntity.ok(result);
    }

    @GetMapping("top-5-type-room")
    public ResponseEntity<?> getTopRoomRevenue(@RequestParam Integer filterOption) {
        return ResponseEntity.ok(roomService.getTopRoomRevenue(filterOption));
    }
}
