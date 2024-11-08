package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.DTO.HotelImageDto;
import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.DTO.TypeRoomImageDto;
import com.hotel.hotel_stars.Models.ImgageModel;
import com.hotel.hotel_stars.Service.ImageService;
import com.hotel.hotel_stars.Service.TypeRoomImageModel;
import com.hotel.hotel_stars.utils.paramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin("*")
@RequestMapping("api/image")
public class ImageController {
    @Autowired
    private ImageService imageService;
    @Autowired
    paramService paramServices;

    @GetMapping("getAll")
    public ResponseEntity<?> getResponseEntity() {
        List<HotelImageDto> images = imageService.getAllImages();
        if (images.isEmpty()) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body( paramServices.messageSuccessApi(400,"error","Không có lấy ra được dữ liệu"));
        }
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

    @DeleteMapping("/delete")
    public ResponseEntity<StatusResponseDto> deleteImages(@RequestBody List<ImgageModel> imgageModels) {
        // Gọi phương thức deleteImage từ service
        StatusResponseDto response = imageService.deleteImage(imgageModels);

        // Kiểm tra kết quả từ response và trả về mã trạng thái phù hợp
        if ("200".equals(response.getCode())) {
            return ResponseEntity.ok(response); // HTTP 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // HTTP 500 Internal Server Error
        }
    }

    @GetMapping("getAllTypeRoom")
    public ResponseEntity<?> getHotelImages() {
        return ResponseEntity.ok(imageService.getAllImageTypes());
    }

    @PostMapping("postTypeImage")
    public ResponseEntity<List<StatusResponseDto>> postHotelImages(@RequestBody List<TypeRoomImageModel> imageModels) {
        try {
            List<StatusResponseDto> responses = imageService.addImageTypes(imageModels);
            return ResponseEntity.ok(responses);
        } catch (DataIntegrityViolationException e) {
            StatusResponseDto response = new StatusResponseDto("400", "FAILURE", "Dữ liệu không hợp lệ.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonList(response));
        } catch (Exception e) {
            StatusResponseDto response = new StatusResponseDto("500", "FAILURE", "Thêm hình ảnh thất bại.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList(response));
        }
    }

    @PutMapping("putTypeImage")
    public ResponseEntity<List<StatusResponseDto>> putHotelImages(@RequestBody List<TypeRoomImageModel> imageModels) {
        try {
            List<StatusResponseDto> responses = imageService.updateImageTypes(imageModels);
            return ResponseEntity.ok(responses);
        } catch (NoSuchElementException e) {
            StatusResponseDto response = new StatusResponseDto("404", "FAILURE", "Không tìm thấy hình ảnh cần cập nhật.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonList(response));
        } catch (DataIntegrityViolationException e) {
            StatusResponseDto response = new StatusResponseDto("400", "FAILURE", "Dữ liệu không hợp lệ.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonList(response));
        } catch (Exception e) {
            StatusResponseDto response = new StatusResponseDto("500", "FAILURE", "Cập nhật hình ảnh thất bại.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList(response));
        }
    }

    @DeleteMapping("delete-image")
    public ResponseEntity<List<StatusResponseDto>> deleteHotelImages(@RequestBody List<ImgageModel> imageModels) {
        if (imageModels == null || imageModels.isEmpty()) {
            StatusResponseDto response = new StatusResponseDto("400", "FAILURE", "Danh sách hình ảnh trống.");
            return ResponseEntity.badRequest().body(Collections.singletonList(response));
        }

        List<StatusResponseDto> results = imageService.deleteByIdImages(imageModels);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/selectById")
    public ResponseEntity<?> selectHotelImageById(@RequestBody List<TypeRoomImageModel> imgageModels) {
        return ResponseEntity.ok(imageService.getTypeRoomImageModelByImageName(imgageModels));
    }

    @GetMapping("get-by-id")
    public ResponseEntity<?> getHotelImageById(@RequestParam Integer id) {
        return ResponseEntity.ok(imageService.getTypeRoomImageModelByTypeRoomId(id));
    }

}
