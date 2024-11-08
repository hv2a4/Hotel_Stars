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

    @GetMapping("get-by-id")
    public ResponseEntity<?> getTypeRoomOverviewById(@RequestParam Integer id) {
        return ResponseEntity.ok(typeRoomOverviewService.seleteTypeRoom(id));
    }

    @GetMapping("get-list-room")
    public ResponseEntity<?> getTypeRoomOverviewList() {
        return ResponseEntity.ok(typeRoomOverviewService.getAllListRoom());
    }
}