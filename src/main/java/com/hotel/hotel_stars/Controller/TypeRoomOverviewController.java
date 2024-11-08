package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.Service.TypeRoomOverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("api/overview/room-types")
public class TypeRoomOverviewController {
    @Autowired
    TypeRoomOverviewService typeRoomOverviewService;

    //Lấy danh sách bảng lỗi phòng và hình ảnh
    @GetMapping("get-all")
    public ResponseEntity<?> getAllTypeRoomOverview() {
        return ResponseEntity.ok(typeRoomOverviewService.getTypeRoomOverview());
    }

    // tìm kiếm danh sách phòng của loại phòng theo ID
    @GetMapping("get-by-id")
    public ResponseEntity<?> getTypeRoomOverviewById(@RequestParam Integer id) {
        return ResponseEntity.ok(typeRoomOverviewService.seleteTypeRoom(id));
    }

    // danh sách đặt phòng
    @GetMapping("get-list-room")
    public ResponseEntity<?> getTypeRoomOverviewList() {
        return ResponseEntity.ok(typeRoomOverviewService.getAllListRoom());
    }

    //lịch đặt phòng
    @GetMapping("booking-history")
    public ResponseEntity<?> getTypeRoomOverviewBookingHistory(@RequestParam Integer id) {
        return ResponseEntity.ok(typeRoomOverviewService.getRoomReservationList(id));
    }

    @GetMapping("bed-type-options")
    public ResponseEntity<?> getTypeRoomOverviewBedTypeOptions() {
        return ResponseEntity.ok(typeRoomOverviewService.getTypeBedList());
    }
}
