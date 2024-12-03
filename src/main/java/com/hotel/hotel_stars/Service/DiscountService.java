package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.DiscountDto;
import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.Entity.Discount;
import com.hotel.hotel_stars.Entity.TypeRoom;
import com.hotel.hotel_stars.Models.DiscountModel;
import com.hotel.hotel_stars.Repository.DiscountRepository;
import com.hotel.hotel_stars.Repository.TypeRoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DiscountService {
    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    TypeRoomRepository typeRoomRepository;

    public List<DiscountDto> getDiscountByAccount(String userName) {
        List<Discount> discounts = discountRepository.findDiscountsByUsername(userName);
       if(discounts == null || discounts.isEmpty()){
           return null;
       }
        return discounts.stream().map(this::convertToDto).toList();
    }

    public DiscountDto convertToDto(Discount discount) {
        DiscountDto discountDto = modelMapper.map(discount, DiscountDto.class);
        return discountDto;
    }

    public List<DiscountDto> getAllDiscountDtos() {
        List<Discount> discounts = discountRepository.findAll();
        return discounts.stream().map(this::convertToDto).toList();
    }

    public List<DiscountDto> getDiscountByTypeRoom(Integer id) {
        List<Discount> discounts = discountRepository.findActiveDiscountsForTypeRoom(id);
        return discounts.stream().map(this::convertToDto).toList();
    }

    public DiscountDto findDiscountDtoById(Integer id) {
        Discount discount = discountRepository.findById(id).orElse(null);
        return convertToDto(discount);
    }

    public StatusResponseDto saveDiscountDto(DiscountModel discountModel) {
        // Lấy ngày hiện tại
        LocalDate today = LocalDate.now();

        // Chuyển đổi thời gian bắt đầu và kết thúc thành LocalDate
        LocalDate startDate = discountModel.getStartDate().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = discountModel.getEndDate().atZone(ZoneId.systemDefault()).toLocalDate();

        // Kiểm tra điều kiện ngày và phần trăm giảm giá
        if (startDate.isBefore(today)) {
            return new StatusResponseDto("400", "FAILURE", "Ngày bắt đầu không được ở trong quá khứ.");
        }

        if (discountModel.getPercent() <= 0 || discountModel.getPercent() > 100) {
            return new StatusResponseDto("400", "FAILURE", "Phần trăm giảm giá phải lớn hơn 0 và không vượt quá 100.");
        }

        if (endDate.isBefore(startDate)) {
            return new StatusResponseDto("400", "FAILURE", "Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu.");
        }

        // Kiểm tra loại phòng
        TypeRoom typeRoom = typeRoomRepository.findById(discountModel.getTypeRoomId()).orElse(null);
        if (typeRoom == null) {
            return new StatusResponseDto("404", "FAILURE", "Loại phòng không tồn tại.");
        } else {
            // Kiểm tra thời gian trùng lặp với giảm giá khác
            List<Discount> existingDiscounts = discountRepository.findByRoomTypeId(typeRoom);
            for (Discount existingDiscount : existingDiscounts) {
                LocalDate existingStartDate = existingDiscount.getStartDate().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate existingEndDate = existingDiscount.getEndDate().atZone(ZoneId.systemDefault()).toLocalDate();

                if (!(endDate.isBefore(existingStartDate) || startDate.isAfter(existingEndDate))) {
                    return new StatusResponseDto("400", "FAILURE", "Khoảng thời gian giảm giá bị trùng lặp với một giảm giá khác.");
                }
            }
        }

        // Nếu tất cả điều kiện hợp lệ, tiếp tục lưu dữ liệu
        try {
            Discount discount = new Discount();
            discount.setDiscountName(discountModel.getDiscountName());
            discount.setPercent(discountModel.getPercent());
            discount.setStartDate(discountModel.getStartDate());
            discount.setEndDate(discountModel.getEndDate());
            discount.setTypeRoom(typeRoom);

            discountRepository.save(discount);
            return new StatusResponseDto("200", "SUCCESS", "Thêm giảm giá thành công.");
        } catch (Exception e) {
            return new StatusResponseDto("500", "FAILURE", "Lỗi trong quá trình thêm giảm giá.");
        }
    }



    public DiscountDto updateDiscountDto(DiscountModel discountModel) {
        // Kiểm tra xem giảm giá có tồn tại không
        Optional<Discount> optionalDiscount = discountRepository.findById(discountModel.getId());
        if (optionalDiscount.isEmpty()) {
            throw new NoSuchElementException("Không tìm thấy giảm giá với ID: " + discountModel.getId());
        }

        Discount discount = optionalDiscount.get();

        // Lấy ngày hiện tại
        LocalDate today = LocalDate.now();

        // Chuyển đổi thời gian bắt đầu và kết thúc thành LocalDate
        LocalDate startDate = discountModel.getStartDate().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = discountModel.getEndDate().atZone(ZoneId.systemDefault()).toLocalDate();

        // Kiểm tra điều kiện ngày và phần trăm giảm giá
        if (startDate.isBefore(today)) {
            throw new IllegalArgumentException("Ngày bắt đầu không được ở trong quá khứ.");
        }

        if (discountModel.getPercent() <= 0 || discountModel.getPercent() > 100) {
            throw new IllegalArgumentException("Phần trăm giảm giá phải lớn hơn 0 và không vượt quá 100.");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu.");
        }

        // Kiểm tra loại phòng tồn tại
        TypeRoom typeRoom = typeRoomRepository.findById(discountModel.getTypeRoomId())
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy loại phòng với ID: " + discountModel.getTypeRoomId()));

        // Kiểm tra thời gian trùng lặp với các giảm giá khác
        List<Discount> existingDiscounts = discountRepository.findByRoomTypeId(typeRoom);
        for (Discount existingDiscount : existingDiscounts) {
            if (!existingDiscount.getId().equals(discountModel.getId())) { // Bỏ qua chính giảm giá đang cập nhật
                LocalDate existingStartDate = existingDiscount.getStartDate().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate existingEndDate = existingDiscount.getEndDate().atZone(ZoneId.systemDefault()).toLocalDate();

                if (!(endDate.isBefore(existingStartDate) || startDate.isAfter(existingEndDate))) {
                    throw new IllegalArgumentException("Khoảng thời gian giảm giá bị trùng lặp với một giảm giá khác.");
                }
            }
        }

        // Nếu tất cả điều kiện hợp lệ, tiếp tục cập nhật thông tin giảm giá
        discount.setDiscountName(discountModel.getDiscountName());
        discount.setPercent(discountModel.getPercent());
        discount.setStartDate(discountModel.getStartDate());
        discount.setEndDate(discountModel.getEndDate());
        discount.setTypeRoom(typeRoom);

        discountRepository.save(discount);
        return convertToDto(discount);
    }


    public StatusResponseDto deletById(Integer id) {
        // Kiểm tra xem giảm giá có tồn tại trước khi xóa
        if (!discountRepository.existsById(id)) {
            return new StatusResponseDto("404", "FAILURE", "Không tìm thấy giảm giá với ID: " + id);
        }

        try {
            discountRepository.deleteById(id);
            return new StatusResponseDto("200", "SUCCESS", "Xóa giảm giá thành công!");
        } catch (Exception e) {
            // Xử lý lỗi nếu có vấn đề khi xóa
            return new StatusResponseDto("500", "FAILURE", "Lỗi trong quá trình xóa giảm giá.");
        }
    }


}
