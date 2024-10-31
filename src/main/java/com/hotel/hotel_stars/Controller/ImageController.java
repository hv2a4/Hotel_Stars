package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.DTO.HotelImageDto;
import com.hotel.hotel_stars.Models.ImgageModel;
import com.hotel.hotel_stars.Service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("api/image")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @GetMapping("getAll")
    public ResponseEntity<?> getResponseEntity() {
        return ResponseEntity.ok(imageService.getAllImages());
    }

    @PostMapping("add-image")
    public ResponseEntity<List<HotelImageDto>> addHotelImages(@RequestBody List<ImgageModel> imageModels) {
        if (imageModels == null || imageModels.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        List<HotelImageDto> savedImages = imageService.addImages(imageModels);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedImages);
    }

    @PutMapping("update-image")
    public ResponseEntity<List<HotelImageDto>> updateHotelImages(@RequestBody List<ImgageModel> imageModels) {
        if (imageModels == null || imageModels.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        List<HotelImageDto> savedImages = imageService.updateImages(imageModels);
        return ResponseEntity.ok(savedImages);
    }

}
