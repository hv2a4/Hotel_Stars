package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.DTO.TypeRoomDto;
import com.hotel.hotel_stars.Service.RoomServiceFilter;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/status")
public class RoomServiceFilterController {

    @Autowired
    private RoomServiceFilter roomServiceFilter;

    @GetMapping("getAll")
    public ResponseEntity<?> getAllStatusRoom() {
        return ResponseEntity.ok(roomServiceFilter.getAllStatusRoom());
    }

    @GetMapping("search-type-room")
    public ResponseEntity<List<TypeRoomDto>> searchTypeRoom(@RequestParam String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        List<TypeRoomDto> typeRoomDtos = roomServiceFilter.searchTypeRoom(keyword);
        return ResponseEntity.ok(typeRoomDtos);
    }
}
