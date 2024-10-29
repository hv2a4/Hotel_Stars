package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.HotelDto;
import com.hotel.hotel_stars.DTO.ServiceRoomDto;
import com.hotel.hotel_stars.Entity.Hotel;
import com.hotel.hotel_stars.Entity.ServiceRoom;
import com.hotel.hotel_stars.Models.serviceRoomModel;
import com.hotel.hotel_stars.Repository.ServiceRoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ServiceRoomService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ServiceRoomRepository srrep;

    public ServiceRoomDto convertServiceRoomDto(ServiceRoom sr) {
        return new ServiceRoomDto(sr.getId(), sr.getServiceRoomName(), sr.getPrice());
    }

    public List<ServiceRoomDto> getAllServiceRooms() {
        List<ServiceRoom> srs = srrep.findAll();
        return srs.stream()
                .map(this::convertServiceRoomDto)
                .toList();
    }

    // thêm dịch vụ phòng
    public ResponseEntity<?> addServiceRoom(ServiceRoomDto serviceRoomDto) {
        Map<String, List<String>> errors = new HashMap<>();

        // checkvalidate tên dịch vụ phòng
        if (serviceRoomDto.getServiceRoomName() == null || serviceRoomDto.getServiceRoomName().isEmpty()) {
            errors.computeIfAbsent("serviceRoomName", k -> new ArrayList<>()).add("Tên dịch vụ phòng không được để trống");
        }

        // checkvalidate giá dịch vụ phòng
        if (serviceRoomDto.getPrice() == null) {
            errors.computeIfAbsent("price", k -> new ArrayList<>()).add("Giá dịch vụ phòng không được để trống");
        } else if (serviceRoomDto.getPrice() <= 0) {
            errors.computeIfAbsent("price", k -> new ArrayList<>()).add("Giá dịch vụ phòng phải lớn hơn 0");
        }

        if (!errors.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // lưu thông tin
        ServiceRoom serviceRoom = new ServiceRoom();
        serviceRoom.setServiceRoomName(serviceRoomDto.getServiceRoomName());
        serviceRoom.setPrice(serviceRoomDto.getPrice());

        ServiceRoom savedServiceRoom = srrep.save(serviceRoom);
        return new ResponseEntity<>(convertServiceRoomDto(savedServiceRoom), HttpStatus.CREATED);
    }

    // cập nhật dịch vụ phòng
    public ResponseEntity<?> updateServiceRoom(Integer id, serviceRoomModel srmodel) {
        Map<String, List<String>> errors = new HashMap<>();

        // checkvalidate tra tên dịch vụ phòng
        if (srmodel.getServiceRoomName() == null || srmodel.getServiceRoomName().isEmpty()) {
            errors.computeIfAbsent("serviceRoomName", k -> new ArrayList<>()).add("Tên dịch vụ phòng không được để trống");
        }

        // checkvalidate giá dịch vụ phòng
        if (srmodel.getPrice() == null) {
            errors.computeIfAbsent("price", k -> new ArrayList<>()).add("Giá dịch vụ phòng không được để trống");
        } else if (srmodel.getPrice() <= 0) {
            errors.computeIfAbsent("price", k -> new ArrayList<>()).add("Giá dịch vụ phòng phải lớn hơn 0");
        }

        // checkvalidate id phòng
        ServiceRoom sr = srrep.findById(id).orElse(null);
        if (sr == null) {
            errors.computeIfAbsent("id", k -> new ArrayList<>()).add("Dịch vụ phòng với ID " + id + " không tồn tại");
        }

        // check lỗi nếu có
        if (!errors.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // lưu lại sau khi cập nhật
        sr.setServiceRoomName(srmodel.getServiceRoomName());
        sr.setPrice(srmodel.getPrice());
        return new ResponseEntity<>(convertServiceRoomDto(srrep.save(sr)), HttpStatus.OK);
    }

    // xóa dịch vụ phòng
    public Map<String, String> deleleServiceRoom(Integer id) {
        Map<String, String> result = new HashMap<>();
        // checkvalidate sự tồn tại id
        ServiceRoom sr = srrep.findById(id).orElse(null);
        if (sr == null) {
            throw new IllegalArgumentException("Dịch vụ phòng với ID " + id + " không tồn tại");
        }

        srrep.delete(sr);
        result.put("Status", "Xóa thành công");
        return result;
    }
}
