package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.DTO.Select.RoomAvailabilityInfo;
import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.Entity.Room;
import com.hotel.hotel_stars.Models.RoomModel;
import com.hotel.hotel_stars.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/room")
@CrossOrigin("*")
public class RoomController {
    @Autowired
    private RoomService roomService;

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
}
