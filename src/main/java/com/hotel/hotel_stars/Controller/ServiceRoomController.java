package com.hotel.hotel_stars.Controller;



import com.hotel.hotel_stars.DTO.ServiceRoomDto;
import com.hotel.hotel_stars.Entity.ServiceRoom;
import com.hotel.hotel_stars.Service.ServiceRoomService;
import com.hotel.hotel_stars.Models.serviceRoomModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/add")
    public ResponseEntity<?> addServiceRoom(@RequestBody ServiceRoomDto serviceRoomDto) {
        return ResponseEntity.ok(srservice.addServiceRoom(serviceRoomDto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateServiceRoom(@PathVariable("id") Integer sr_id, @RequestBody serviceRoomModel srmodel) {
        return srservice.updateServiceRoom(sr_id, srmodel);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteServiceRoom(@PathVariable("id") Integer sr_id) {
        return ResponseEntity.ok(srservice.deleleServiceRoom(sr_id));
    }


}
