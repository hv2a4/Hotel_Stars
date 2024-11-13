package com.hotel.hotel_stars.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.hotel_stars.Models.typeRoomServiceModel;
import com.hotel.hotel_stars.Service.ServiceTypeRoomService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin("*")
@RequestMapping("api/type-room-service")
public class TypeRoomServiceController {
	@Autowired
	ServiceTypeRoomService service;
	
	@GetMapping("/getAll")
	public ResponseEntity<?> getAll(){
		return ResponseEntity.ok(service.getAll());
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> createTypeRoomService(@RequestBody typeRoomServiceModel model) {
		return ResponseEntity.ok(service.createTypeServiceRoom(model));
	}
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateTypeRoomService(@PathVariable("id") Integer id, @RequestBody typeRoomServiceModel model) {
		return ResponseEntity.ok(service.updateTypeServiceRoom(id,model));
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteTypeRoomService(@PathVariable Integer id) {
	    try {
	    	service.deleteTypeRoomService(id);
	        return ResponseEntity.ok("Xóa thành công!");
	    } catch (DataIntegrityViolationException e) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body("Không thể xóa vì đang có dữ liệu liên quan!");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra trong quá trình xóa!");
	    }
	}

}
