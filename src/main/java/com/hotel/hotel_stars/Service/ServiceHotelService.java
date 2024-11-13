package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.HotelDto;
import com.hotel.hotel_stars.DTO.ServiceHotelDto;
import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.Entity.Hotel;
import com.hotel.hotel_stars.Entity.ServiceHotel;
import com.hotel.hotel_stars.Models.ServiceHotelModel;
import com.hotel.hotel_stars.Repository.HotelRepository;
import com.hotel.hotel_stars.Repository.ServiceHotelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceHotelService {
    @Autowired
    ServiceHotelRepository serviceHotelRepository;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    ModelMapper modelMapper;

    public ServiceHotelDto convertToDto(ServiceHotel serviceHotel) {
        Hotel hotel = hotelRepository.findById(1)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));

        serviceHotel.setHotel(hotel);

        return modelMapper.map(serviceHotel, ServiceHotelDto.class);
    }

    public List<ServiceHotelDto> getAllServiceHotels() {
        List<ServiceHotel> serviceHotels = serviceHotelRepository.findAll();
        return serviceHotels.stream().map(this::convertToDto).toList();
    }

    public ServiceHotelDto getAllServiceHotelsByHotelId(Integer Id) {
        Optional<ServiceHotel> serviceHotels = serviceHotelRepository.findById(Id);
        if (serviceHotels.isPresent()) {
            return convertToDto(serviceHotels.get());
        }
        return null;
    }

    public StatusResponseDto addServiceHotel(ServiceHotelModel hotelModel) {
        StatusResponseDto statusResponseDto = new StatusResponseDto();

        try {
            ServiceHotel serviceHotel = new ServiceHotel();
            Hotel hotel = hotelRepository.findById(1).orElseThrow(() -> new EntityNotFoundException("Hotel not found"));

            serviceHotel.setHotel(hotel);
            serviceHotel.setServiceHotelName(hotelModel.getServiceHotelName());
            serviceHotel.setPrice(hotelModel.getPrice());
            serviceHotel.setImageName(hotelModel.getImage());
            serviceHotelRepository.save(serviceHotel);

            // Cài đặt trạng thái thành công
            statusResponseDto.setCode("200");
            statusResponseDto.setStatus("THÀNH CÔNG");
            statusResponseDto.setMessage("Dịch vụ khách sạn đã được thêm thành công.");

        } catch (EntityNotFoundException e) {
            statusResponseDto.setCode("404");
            statusResponseDto.setStatus("LỖI");
            statusResponseDto.setMessage("Không tìm thấy khách sạn: " + e.getMessage());

        } catch (DataIntegrityViolationException e) {
            statusResponseDto.setCode("400");
            statusResponseDto.setStatus("LỖI");
            statusResponseDto.setMessage("Dữ liệu không hợp lệ: " + e.getMessage());

        } catch (Exception e) {
            statusResponseDto.setCode("500");
            statusResponseDto.setStatus("LỖI");
            statusResponseDto.setMessage("Lỗi không xác định: " + e.getMessage());
        }

        return statusResponseDto;
    }

    public StatusResponseDto updateServiceHotel(ServiceHotelModel hotelModel) {
        StatusResponseDto statusResponseDto = new StatusResponseDto();

        try {
            ServiceHotel serviceHotel = serviceHotelRepository.findById(hotelModel.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Service Hotel not found"));
            Hotel hotel = hotelRepository.findById(1).orElseThrow(() -> new EntityNotFoundException("Hotel not found"));

            serviceHotel.setHotel(hotel);
            serviceHotel.setServiceHotelName(hotelModel.getServiceHotelName());
            serviceHotel.setPrice(hotelModel.getPrice());
            serviceHotel.setImageName(hotelModel.getImage());

            serviceHotelRepository.save(serviceHotel);

            statusResponseDto.setCode("200");
            statusResponseDto.setStatus("THÀNH CÔNG");
            statusResponseDto.setMessage("Dịch vụ khách sạn đã được cập nhật thành công.");

        } catch (EntityNotFoundException e) {
            statusResponseDto.setCode("404");
            statusResponseDto.setStatus("LỖI");
            statusResponseDto.setMessage("Không tìm thấy khách sạn hoặc dịch vụ: " + e.getMessage());

        } catch (DataIntegrityViolationException e) {
            statusResponseDto.setCode("400");
            statusResponseDto.setStatus("LỖI");
            statusResponseDto.setMessage("Dữ liệu không hợp lệ: " + e.getMessage());

        } catch (Exception e) {
            statusResponseDto.setCode("500");
            statusResponseDto.setStatus("LỖI");
            statusResponseDto.setMessage("Lỗi không xác định: " + e.getMessage());
        }

        return statusResponseDto;
    }

    public StatusResponseDto deleteById(Integer id) {
        StatusResponseDto statusResponseDto = new StatusResponseDto();

        try {
            // Kiểm tra xem dịch vụ có tồn tại trước khi xóa không
            if (serviceHotelRepository.existsById(id)) {
                serviceHotelRepository.deleteById(id);

                // Trả về trạng thái thành công
                statusResponseDto.setCode("200");
                statusResponseDto.setStatus("OK");
                statusResponseDto.setMessage("Xóa dịch vụ khách sạn thành công!");
            } else {
                // Trả về thông báo lỗi nếu không tìm thấy ID
                statusResponseDto.setCode("404");
                statusResponseDto.setStatus("NOT FOUND");
                statusResponseDto.setMessage("Không tìm thấy dịch vụ khách sạn với ID: " + id);
            }
        } catch (EmptyResultDataAccessException e) {
            // Lỗi khi cố xóa một ID không tồn tại
            statusResponseDto.setCode("404");
            statusResponseDto.setStatus("NOT FOUND");
            statusResponseDto.setMessage("Không tìm thấy dịch vụ khách sạn để xóa");
        } catch (DataIntegrityViolationException e) {
            // Lỗi về ràng buộc dữ liệu
            statusResponseDto.setCode("400");
            statusResponseDto.setStatus("BAD REQUEST");
            statusResponseDto.setMessage("Không thể xóa vì có dữ liệu liên quan ");
        } catch (Exception e) {
            // Lỗi không xác định
            statusResponseDto.setCode("500");
            statusResponseDto.setStatus("INTERNAL SERVER ERROR");
            statusResponseDto.setMessage("Lỗi không xác định");
        }

        return statusResponseDto;
    }

}
