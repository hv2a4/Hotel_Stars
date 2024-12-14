package com.hotel.hotel_stars.Controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.hotel_stars.DTO.HotelImageDto;
import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.Entity.TypeRoom;
import com.hotel.hotel_stars.Entity.TypeRoomImage;
import com.hotel.hotel_stars.Models.ImgageModel;
import com.hotel.hotel_stars.Repository.TypeRoomImageRepository;
import com.hotel.hotel_stars.Repository.TypeRoomRepository;
import com.hotel.hotel_stars.Service.ImageService;
import com.hotel.hotel_stars.Service.TypeRoomImageModel;
import com.hotel.hotel_stars.utils.paramService;

@RestController
@CrossOrigin("*")
@RequestMapping("api/image")
public class ImageController {
	@Autowired
	private ImageService imageService;
	@Autowired
	paramService paramServices;
    @Autowired
    private TypeRoomImageRepository typeRoomImageRepository;
    @Autowired
    private TypeRoomRepository typeRoomRepository;

	@GetMapping("getAll")
	public ResponseEntity<?> getResponseEntity() {
		List<HotelImageDto> images = imageService.getAllImages();
		if (images.isEmpty()) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(paramServices.messageSuccessApi(400, "error", "Không có lấy ra được dữ liệu"));
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
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // HTTP 500 Internal Server
																							// Error
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
	public StatusResponseDto updateImageType(@RequestBody TypeRoomImageModel typeRoomImageModel) {
		StatusResponseDto response;
		try {
			Optional<TypeRoomImage> optionalTypeRoomImage = typeRoomImageRepository
					.findById(typeRoomImageModel.getId());
			Optional<TypeRoom> optionalTypeRoom = typeRoomRepository.findById(typeRoomImageModel.getTypeRoom_Id());

			if (optionalTypeRoomImage.isPresent() && optionalTypeRoom.isPresent()) {
				TypeRoomImage typeRoomImage = optionalTypeRoomImage.get();
				typeRoomImage.setTypeRoom(optionalTypeRoom.get());
				typeRoomImage.setImageName(typeRoomImageModel.getImageName());
				TypeRoomImage savedImage = typeRoomImageRepository.save(typeRoomImage);
				response = new StatusResponseDto("200", "SUCCESS",
						"Cập nhật thành công hình ảnh ID: " + savedImage.getId());
			} else if (!optionalTypeRoomImage.isPresent()) {
				response = new StatusResponseDto("404", "NOT_FOUND",
						"Không tìm thấy TypeRoomImage với ID: " + typeRoomImageModel.getId());
			} else {
				response = new StatusResponseDto("404", "NOT_FOUND",
						"Không tìm thấy TypeRoom với ID: " + typeRoomImageModel.getTypeRoom_Id());
			}
		} catch (DataIntegrityViolationException e) {
			response = new StatusResponseDto("400", "BAD_REQUEST",
					"Dữ liệu không hợp lệ cho hình ảnh ID: " + typeRoomImageModel.getId());
		} catch (Exception e) {
			response = new StatusResponseDto("500", "INTERNAL_SERVER_ERROR",
					"Lỗi hệ thống khi cập nhật hình ảnh ID: " + typeRoomImageModel.getId());
		}

		return response;
	}


	@DeleteMapping("delete-image")
	public ResponseEntity<StatusResponseDto> deleteHotelImage(@RequestBody TypeRoomImageModel imageModel) {
		if (imageModel == null) {
			StatusResponseDto response = new StatusResponseDto("400", "FAILURE", "Thông tin hình ảnh không hợp lệ.");
			return ResponseEntity.badRequest().body(response);
		}

		StatusResponseDto result = imageService.deleteByIdImage(imageModel);
		return ResponseEntity.ok(result);
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
