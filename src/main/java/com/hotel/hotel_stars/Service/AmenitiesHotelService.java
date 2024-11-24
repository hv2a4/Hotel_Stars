package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.AmenitiesHotelDto;
import com.hotel.hotel_stars.DTO.HotelDto;
import com.hotel.hotel_stars.DTO.TypeAmenitiesHotelDto;
import com.hotel.hotel_stars.Entity.AmenitiesHotel;
import com.hotel.hotel_stars.Entity.Hotel;
import com.hotel.hotel_stars.Models.amenitiesHotelModel;
import com.hotel.hotel_stars.Repository.AmenitiesHotelRepository;
import com.hotel.hotel_stars.Repository.HotelRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AmenitiesHotelService {
    @Autowired
    private AmenitiesHotelRepository amenitiesHotelRepository;

    @Autowired
    private HotelRepository hotelRepository;

    // chuyển đổi entity sang dto (đổ dữ liệu lên web)
    public AmenitiesHotelDto convertAmenitiesHotelDto(AmenitiesHotel ah) {
        return new AmenitiesHotelDto(
                ah.getId(),
                ah.getAmenitiesHotelName()
        );
    }

    // Hiển thị danh sách dịch vụ phòng
    public List<AmenitiesHotelDto> getAllAmenitiesHotels() {
        List<AmenitiesHotel> ahs = amenitiesHotelRepository.findAll();
        return ahs.stream().map(this::convertAmenitiesHotelDto).toList();
    }

    public boolean checkIfExistsByName(String name) {
        return amenitiesHotelRepository.existsByAmenitiesHotelName(name);
    }

    public AmenitiesHotelDto getAmenitiesHotelById(Integer id) {
        Optional<AmenitiesHotel> ahOpt = amenitiesHotelRepository.findById(id);
        if (ahOpt.isEmpty()) {
            throw new NoSuchElementException("Dịch vụ phòng này không tồn tại"); // Ném ngoại lệ nếu không tìm thấy
        }
        return convertAmenitiesHotelDto(ahOpt.get()); // Trả về đối tượng DTO sau khi tìm thấy
    }

    // thêm dịch vụ phòng
    public AmenitiesHotelDto addAmenitiesHotel(amenitiesHotelModel amenitiesHotelModel) {
        List<String> errorMessages = new ArrayList<>(); // Danh sách lưu trữ các thông báo lỗi

        // Kiểm tra tên loại phòng
        if (amenitiesHotelModel.getAmenitiesHotelName() == null || amenitiesHotelModel.getAmenitiesHotelName().isEmpty()) {
            errorMessages.add("Tên tiện nghi khách sạn không được để trống");
        } else if (amenitiesHotelRepository.existsByAmenitiesHotelName(amenitiesHotelModel.getAmenitiesHotelName())) {
            errorMessages.add("Tên tiện nghi khách sạn này đã tồn tại");
        }

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(String.join(", ", errorMessages));
        }

        try {
            AmenitiesHotel amenitiesHotel = new AmenitiesHotel();

            // Đặt thông tin loại phòng
            amenitiesHotel.setAmenitiesHotelName(amenitiesHotelModel.getAmenitiesHotelName());
            // Đặt hotelId mặc định là 1
            Optional<Hotel> hotel = hotelRepository.findById(1);
            // Kiểm tra nếu không tìm thấy hotel với id = 1
            if (hotel.isPresent()) {
                amenitiesHotel.setHotel(hotel.get());
            } else {
                throw new ValidationException("Không tìm thấy khách sạn với ID: 1");
            }
            // Lưu thông tin loại phòng vào cơ sở dữ liệu
            AmenitiesHotel savedAmenitiesHotel = amenitiesHotelRepository.save(amenitiesHotel);
            // Chuyển đổi và trả về DTO
            return convertAmenitiesHotelDto(savedAmenitiesHotel);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Có lỗi xảy ra do vi phạm tính toàn vẹn dữ liệu", e);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException("Có lỗi xảy ra khi thêm!", e);
        }
    }

    // cập nhật dịch vụ phòng
    public AmenitiesHotelDto updateAmenitiesHotel(amenitiesHotelModel amenitiesHotelModel) {
        List<String> errorMessages = new ArrayList<>(); // Danh sách lưu trữ các thông báo lỗi

        // Kiểm tra xem loại phòng có tồn tại hay không
        Optional<AmenitiesHotel> existingAmenitiesHotelOpt = amenitiesHotelRepository.findById(amenitiesHotelModel.getId());
        if (!existingAmenitiesHotelOpt.isPresent()) {
            throw new EntityNotFoundException("Tiện ích khách sạn với ID " + amenitiesHotelModel.getId() + " không tồn tại.");
        }
        AmenitiesHotel existingAmenitiesHotel = existingAmenitiesHotelOpt.get();

        // Kiểm tra tên loại phòng
        if (amenitiesHotelModel.getAmenitiesHotelName() == null || amenitiesHotelModel.getAmenitiesHotelName().isEmpty()) {
            errorMessages.add("Tên tiện nghi khách sạn không được để trống");
        } else if (!existingAmenitiesHotel.getAmenitiesHotelName().equals(amenitiesHotelModel.getAmenitiesHotelName()) && amenitiesHotelRepository.existsByAmenitiesHotelName(amenitiesHotelModel.getAmenitiesHotelName())) {
            errorMessages.add("Tên tiện nghi khách sạn này đã tồn tại");
        }


        // Nếu có lỗi, ném ngoại lệ với thông báo lỗi
        if (!errorMessages.isEmpty()) {
            throw new ValidationException(String.join(", ", errorMessages));
        }

        try {
            // Cập nhật các thuộc tính cho loại phòng
            existingAmenitiesHotel.setAmenitiesHotelName(amenitiesHotelModel.getAmenitiesHotelName());
            // Lưu loại phòng đã cập nhật vào cơ sở dữ liệu và chuyển đổi sang DTO
            AmenitiesHotel updatedAmenitiesHotel = amenitiesHotelRepository.save(existingAmenitiesHotel);
            return convertAmenitiesHotelDto(updatedAmenitiesHotel); // Chuyển đổi loại phòng đã lưu sang DTO

        } catch (DataIntegrityViolationException e) {
            // Xử lý lỗi vi phạm tính toàn vẹn dữ liệu
            throw new RuntimeException("Có lỗi xảy ra do vi phạm tính toàn vẹn dữ liệu", e);
        } catch (Exception e) {
            // Xử lý lỗi chung
            throw new RuntimeException("Có lỗi xảy ra khi cập nhật ", e);
        }
    }

    // xóa dịch vụ phòng
    public void deleteTypeRoom(Integer id) {
        if (!amenitiesHotelRepository.existsById(id)) {
            throw new NoSuchElementException("Tiện nghi khách sạn này không tồn tại"); // Ném ngoại lệ nếu không tồn tại
        }
        amenitiesHotelRepository.deleteById(id);
    }
}
