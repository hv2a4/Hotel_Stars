package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.DTO.ServicePackageDto;
import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.Models.ServicePackageModel;
import com.hotel.hotel_stars.Service.ServicePackageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("api/service-package")
public class ServicePackageController {
    @Autowired
    private ServicePackageService servicePackageService;

    @GetMapping("getAll")
    public ResponseEntity<List<ServicePackageDto>> getAll() {
        return ResponseEntity.ok(servicePackageService.getAllServicePageke());
    }

    @GetMapping("get-by-id/{id}")
    public ResponseEntity<ServicePackageDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(servicePackageService.findById(id));
    }

    @PostMapping("post-service-package")
    public ResponseEntity<StatusResponseDto> postServicePackage(@Valid @RequestBody ServicePackageModel servicePackageDto) {
        StatusResponseDto response;
        try {
            response = servicePackageService.addServicePackage(servicePackageDto);
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            response = new StatusResponseDto("400", "Lỗi", "Lỗi vi phạm dữ liệu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response = new StatusResponseDto("500", "Lỗi", "Đã xảy ra lỗi không mong muốn: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("put-service-package")
    public ResponseEntity<StatusResponseDto> putServicePackage(@Valid @RequestBody ServicePackageModel servicePackageDto) {
        StatusResponseDto response;
        try {
            response = servicePackageService.updateServicePackage(servicePackageDto);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            response = new StatusResponseDto("404", "Lỗi", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (DataIntegrityViolationException e) {
            response = new StatusResponseDto("400", "Lỗi", "Lỗi vi phạm dữ liệu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response = new StatusResponseDto("500", "Lỗi", "Đã xảy ra lỗi không mong muốn: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @DeleteMapping("delete-service-package/{id}")
    public ResponseEntity<StatusResponseDto> deleteServicePackage(@Valid @PathVariable("id") Integer id) {
        StatusResponseDto response;
        try {
            response = servicePackageService.deleteServicePackage(id);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            response = new StatusResponseDto("404", "Lỗi", "Không thể xóa gói dịch vụ");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (DataIntegrityViolationException e) {
            response = new StatusResponseDto("400", "Lỗi", "Không thể xóa gói dịch vụ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response = new StatusResponseDto("500", "Lỗi", "Không thể xóa gói dịch vụ");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
