package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.*;
import com.hotel.hotel_stars.Entity.*;
import com.hotel.hotel_stars.Models.ImgageModel;
import com.hotel.hotel_stars.Repository.HotelImageRepository;
import com.hotel.hotel_stars.Repository.HotelRepository;
import com.hotel.hotel_stars.Repository.TypeRoomImageRepository;
import com.hotel.hotel_stars.Repository.TypeRoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {
	@Autowired
	HotelImageRepository hotelImageRepository;

	@Autowired
	HotelRepository hotelRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	TypeRoomImageRepository typeRoomImageRepository;

	@Autowired
	TypeRoomRepository typeRoomRepository;

	// Ánh xạ HotelImage sang HotelImageDto và bao gồm cả HotelDto
	public HotelImageDto convertToDto(HotelImage hotelImage) {
		// Ánh xạ thông tin Hotel sang HotelDto
		HotelDto hotelDto = modelMapper.map(hotelImage.getHotel(), HotelDto.class);

		// Tạo HotelImageDto
		HotelImageDto hotelImageDto = modelMapper.map(hotelImage, HotelImageDto.class);
		hotelImageDto.setHotelDto(hotelDto); // Gán HotelDto cho HotelImageDto

		return hotelImageDto;
	}

	public HotelImage convertToEntity(HotelImageDto hotelImageDto) {
		return modelMapper.map(hotelImageDto, HotelImage.class);
	}

	public List<HotelImageDto> getAllImages() {
		List<HotelImage> images = hotelImageRepository.findAll();
		return images.stream().map(this::convertToDto).toList();
	}

	public List<HotelImageDto> addImages(List<ImgageModel> imgageModels) {
		List<HotelImageDto> hotelImageDtos = new ArrayList<>();

		imgageModels.stream().map(imgageModel -> modelMapper.map(imgageModel, HotelImage.class)).forEach(hotelImage -> {
			Hotel hotel = hotelRepository.findById(1).get();
			hotelImage.setHotel(hotel);
			HotelImage savedHotelImage = hotelImageRepository.save(hotelImage);
			hotelImageDtos.add(convertToDto(savedHotelImage));
		});
		return hotelImageDtos;
	}

	public List<HotelImageDto> updateImages(List<ImgageModel> imgageModels) {
		List<HotelImageDto> hotelImageDtos = new ArrayList<>();

		imgageModels.forEach(imgageModel -> {
			// Kiểm tra xem hình ảnh đã tồn tại trong cơ sở dữ liệu chưa
			HotelImage hotelImage = hotelImageRepository.findById(imgageModel.getId())
					.orElseThrow(() -> new RuntimeException("Hình ảnh không tồn tại với id: " + imgageModel.getId()));

			// Cập nhật thông tin hình ảnh từ ImgageModel
			hotelImage.setImageName(imgageModel.getImageName());

			// Lấy thông tin khách sạn dựa trên ID khách sạn từ hình ảnh
			Hotel hotel = hotelRepository.findById(1).get();

			hotelImage.setHotel(hotel);

			// Lưu cập nhật lại vào cơ sở dữ liệu
			HotelImage updatedHotelImage = hotelImageRepository.save(hotelImage);

			// Thêm DTO của hình ảnh vào danh sách kết quả
			hotelImageDtos.add(convertToDto(updatedHotelImage));
		});

		return hotelImageDtos;
	}

	public StatusResponseDto deleteImage(List<ImgageModel> imgageModels) {
		StatusResponseDto statusResponseDto = new StatusResponseDto();

		try {
			imgageModels.forEach(imgageModel -> {
				hotelImageRepository.deleteById(imgageModel.getId());
			});
			// Trường hợp xóa thành công
			statusResponseDto.setCode("200");
			statusResponseDto.setStatus("THÀNH CÔNG");
			statusResponseDto.setMessage("Xóa ảnh thành công");
		} catch (Exception e) {
			// Trường hợp xóa thất bại
			statusResponseDto.setCode("500");
			statusResponseDto.setStatus("THẤT BẠI");
			statusResponseDto.setMessage("Xóa ảnh thất bại: " + e.getMessage());
		}

		return statusResponseDto;
	}

	// --------------------------------------------------------type room
	// image-----------------------------------------------
	public TypeRoomImageDto toDto(TypeRoomImage typeRoomImage) {
		// Khởi tạo TypeRoomImageDto và ánh xạ các trường cơ bản
		TypeRoomImageDto dto = new TypeRoomImageDto();
		dto.setId(typeRoomImage.getId());
		dto.setImageName(typeRoomImage.getImageName());

		// Kiểm tra và ánh xạ `TypeRoom` nếu không null
		if (typeRoomImage.getTypeRoom() != null) {
			dto.setTypeRoomDto(mapTypeRoomToDto(typeRoomImage.getTypeRoom()));
		}

		return dto;
	}

	// Phương thức phụ để ánh xạ từ TypeRoom sang TypeRoomDto
	private TypeRoomDto mapTypeRoomToDto(TypeRoom typeRoom) {
		TypeRoomDto typeRoomDto = new TypeRoomDto();
		typeRoomDto.setId(typeRoom.getId());
		typeRoomDto.setTypeRoomName(typeRoom.getTypeRoomName());
		typeRoomDto.setPrice(typeRoom.getPrice());
		typeRoomDto.setBedCount(typeRoom.getBedCount());
		typeRoomDto.setAcreage(typeRoom.getAcreage());
		typeRoomDto.setGuestLimit(typeRoom.getGuestLimit());

		// Kiểm tra và ánh xạ `TypeBed` nếu không null
		if (typeRoom.getTypeBed() != null) {
			typeRoomDto.setTypeBedDto(mapTypeBedToDto(typeRoom.getTypeBed()));
		}

		return typeRoomDto;
	}

	// Phương thức phụ để ánh xạ từ TypeBed sang TypeBedDto
	private TypeBedDto mapTypeBedToDto(TypeBed typeBed) {
		TypeBedDto typeBedDto = new TypeBedDto();
		typeBedDto.setId(typeBed.getId());
		typeBedDto.setBedName(typeBed.getBedName());
		return typeBedDto;
	}

	public List<TypeRoomImageDto> getAllImageTypes() {
		List<TypeRoomImage> typeRoomImages = typeRoomImageRepository.findAll();
		return typeRoomImages.stream().map(this::toDto).toList();
	}

	public List<StatusResponseDto> addImageTypes(List<TypeRoomImageModel> typeRoomImageModels) {
		List<StatusResponseDto> responses = new ArrayList<>();

		for (TypeRoomImageModel model : typeRoomImageModels) {
			try {
				Optional<TypeRoom> optionalTypeRoom = typeRoomRepository.findById(model.getTypeRoom_Id());
				if (optionalTypeRoom.isPresent()) {
					TypeRoomImage typeRoomImage = new TypeRoomImage();
					typeRoomImage.setImageName(model.getImageName());
					typeRoomImage.setTypeRoom(optionalTypeRoom.get());
					TypeRoomImage savedImage = typeRoomImageRepository.save(typeRoomImage);
					responses.add(new StatusResponseDto("200", "SUCCESS",
							"Thêm thành công hình ảnh ID: " + savedImage.getId()));
				} else {
					responses.add(new StatusResponseDto("404", "NOT_FOUND",
							"Không tìm thấy TypeRoom với ID: " + model.getTypeRoom_Id()));
				}
			} catch (DataIntegrityViolationException e) {
				responses.add(new StatusResponseDto("400", "BAD_REQUEST",
						"Dữ liệu không hợp lệ cho hình ảnh: " + model.getImageName()));
			} catch (Exception e) {
				responses.add(new StatusResponseDto("500", "INTERNAL_SERVER_ERROR",
						"Lỗi hệ thống khi thêm hình ảnh: " + model.getImageName()));
			}
		}

		return responses;
	}

	public List<StatusResponseDto> updateImageTypes(List<TypeRoomImageModel> typeRoomImageModels) {
		List<StatusResponseDto> responses = new ArrayList<>();

		for (TypeRoomImageModel typeRoomImageModel : typeRoomImageModels) {
			try {
				Optional<TypeRoomImage> optionalTypeRoomImage = typeRoomImageRepository
						.findById(typeRoomImageModel.getId());
				Optional<TypeRoom> optionalTypeRoom = typeRoomRepository.findById(typeRoomImageModel.getTypeRoom_Id());

				if (optionalTypeRoomImage.isPresent() && optionalTypeRoom.isPresent()) {
					TypeRoomImage typeRoomImage = optionalTypeRoomImage.get();
					typeRoomImage.setTypeRoom(optionalTypeRoom.get());
					typeRoomImage.setImageName(typeRoomImageModel.getImageName());
					TypeRoomImage savedImage = typeRoomImageRepository.save(typeRoomImage);
					responses.add(new StatusResponseDto("200", "SUCCESS",
							"Cập nhật thành công hình ảnh ID: " + savedImage.getId()));
				} else if (!optionalTypeRoomImage.isPresent()) {
					responses.add(new StatusResponseDto("404", "NOT_FOUND",
							"Không tìm thấy TypeRoomImage với ID: " + typeRoomImageModel.getId()));
				} else {
					responses.add(new StatusResponseDto("404", "NOT_FOUND",
							"Không tìm thấy TypeRoom với ID: " + typeRoomImageModel.getTypeRoom_Id()));
				}
			} catch (DataIntegrityViolationException e) {
				responses.add(new StatusResponseDto("400", "BAD_REQUEST",
						"Dữ liệu không hợp lệ cho hình ảnh ID: " + typeRoomImageModel.getId()));
			} catch (Exception e) {
				responses.add(new StatusResponseDto("500", "INTERNAL_SERVER_ERROR",
						"Lỗi hệ thống khi cập nhật hình ảnh ID: " + typeRoomImageModel.getId()));
			}
		}

		return responses;
	}

	public StatusResponseDto deleteByIdImage(TypeRoomImageModel imageModel) {
		try {
			typeRoomImageRepository.deleteById(imageModel.getId());
			return new StatusResponseDto("200", "SUCCESS", "Xóa thành công hình ảnh ID: " + imageModel.getId());
		} catch (DataAccessException e) {
			return new StatusResponseDto("500", "FAILURE", "Lỗi cơ sở dữ liệu khi xóa hình ảnh ID: " + imageModel.getId());
		} catch (Exception e) {
			return new StatusResponseDto("500", "FAILURE", "Xóa thất bại hình ảnh ID: " + imageModel.getId());
		}
	}

	public List<TypeRoomImageDto> getTypeRoomImageModelByImageName(List<TypeRoomImageModel> typeRoomImageModels) {
		List<TypeRoomImageDto> results = new ArrayList<>();
		typeRoomImageModels.forEach(typeRoomImageModel -> {
			TypeRoomImage typeRoomImage = typeRoomImageRepository.findById(typeRoomImageModel.getId()).get();
			results.add(toDto(typeRoomImage));
		});
		return results;
	}

    public List<TypeRoomImageDto> getTypeRoomImageModelByTypeRoomId(Integer typeRoomId) {
		List <TypeRoomImage> room = typeRoomImageRepository.findByTypeRoomId(typeRoomId);
        return room.stream().map(this::toDto).toList();
    }

}
