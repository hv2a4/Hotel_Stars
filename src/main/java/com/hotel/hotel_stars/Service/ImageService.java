package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.HotelDto;
import com.hotel.hotel_stars.DTO.HotelImageDto;
import com.hotel.hotel_stars.Entity.Hotel;
import com.hotel.hotel_stars.Entity.HotelImage;
import com.hotel.hotel_stars.Models.ImgageModel;
import com.hotel.hotel_stars.Repository.HotelImageRepository;
import com.hotel.hotel_stars.Repository.HotelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {
    @Autowired
    HotelImageRepository hotelImageRepository;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    ModelMapper modelMapper;

    // Ánh xạ HotelImage sang HotelImageDto và bao gồm cả HotelDto
    public HotelImageDto convertToDto(HotelImage hotelImage) {
        // Ánh xạ thông tin Hotel sang HotelDto
        HotelDto hotelDto = modelMapper.map(hotelImage.getHotel(), HotelDto.class);

        // Tạo HotelImageDto
        HotelImageDto hotelImageDto = modelMapper.map(hotelImage, HotelImageDto.class);
        hotelImageDto.setHotelDto(hotelDto);  // Gán HotelDto cho HotelImageDto

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
        imgageModels.forEach(imgageModel -> {
            // Tạo đối tượng HotelImage từ ImgageModel
            HotelImage hotelImage = modelMapper.map(imgageModel, HotelImage.class);

            // Lấy Hotel dựa trên id của khách sạn từ ImgageModel (idHotel)
            Hotel hotel = hotelRepository.findById(imgageModel.getIdHotel())
                    .orElseThrow(() -> new RuntimeException("Khách sạn không tồn tại với id: " + imgageModel.getIdHotel()));

            hotelImage.setHotel(hotel);

            // Lưu đối tượng HotelImage vào cơ sở dữ liệu
            HotelImage savedHotelImage = hotelImageRepository.save(hotelImage);
            // Thêm đối tượng DTO vào danh sách trả về
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
            Hotel hotel = hotelRepository.findById(imgageModel.getIdHotel())
                    .orElseThrow(() -> new RuntimeException("Khách sạn không tồn tại với id: " + imgageModel.getIdHotel()));

            hotelImage.setHotel(hotel);

            // Lưu cập nhật lại vào cơ sở dữ liệu
            HotelImage updatedHotelImage = hotelImageRepository.save(hotelImage);

            // Thêm DTO của hình ảnh vào danh sách kết quả
            hotelImageDtos.add(convertToDto(updatedHotelImage));
        });

        return hotelImageDtos;
    }
}
