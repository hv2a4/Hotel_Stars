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
        // Lấy thời gian hiện tại
        Instant now = Instant.now();
        Instant oneDayLater = now.plus(1, ChronoUnit.DAYS);

        // Kiểm tra điều kiện thời gian và phần trăm giảm giá
        if (discountModel.getStartDate().isBefore(now)) {
            return new StatusResponseDto("400", "FAILURE", "Ngày bắt đầu không được ở trong quá khứ.");
        }
        if (discountModel.getStartDate().isBefore(now.minus(1, ChronoUnit.DAYS))) {
            return new StatusResponseDto("400", "FAILURE", "Ngày bắt đầu không thể là quá khứ xa hơn 1 ngày.");
        }
        if (discountModel.getEndDate().isBefore(discountModel.getStartDate())) {
            return new StatusResponseDto("400", "FAILURE", "Ngày kết thúc phải sau ngày bắt đầu.");
        }
        if (discountModel.getEndDate().isBefore(oneDayLater) && discountModel.getEndDate().equals(discountModel.getStartDate())) {
            return new StatusResponseDto("400", "FAILURE", "Ngày kết thúc không thể trùng với ngày bắt đầu.");
        }

        // Kiểm tra khoảng cách ít nhất là 24 giờ (bao gồm giờ, phút, giây)
        long secondsBetween = ChronoUnit.SECONDS.between(discountModel.getStartDate(), discountModel.getEndDate());
        if (secondsBetween < 86400) { // 86400 giây = 24 giờ
            return new StatusResponseDto("400", "FAILURE", "Khoảng thời gian giữa ngày bắt đầu và ngày kết thúc phải ít nhất 24 giờ.");
        }

        if (discountModel.getPercent() <= 0 || discountModel.getPercent() > 100) {
            return new StatusResponseDto("400", "FAILURE", "Phần trăm giảm giá phải lớn hơn 0 và không vượt quá 100.");
        }

        // Kiểm tra loại phòng
        TypeRoom typeRoom = typeRoomRepository.findById(discountModel.getTypeRoomId()).orElse(null);
        if (typeRoom == null) {
            return new StatusResponseDto("404", "FAILURE", "Loại phòng không tồn tại.");
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
        Optional<Discount> optionalDiscount = discountRepository.findById(discountModel.getId());
        if (optionalDiscount.isEmpty()) {
            throw new NoSuchElementException("Không tìm thấy giảm giá với ID: " + discountModel.getId());
        }

        Discount discount = optionalDiscount.get();

        // Kiểm tra phần trăm giảm giá hợp lệ
        if (discountModel.getPercent() < 0 || discountModel.getPercent() > 100) {
            throw new IllegalArgumentException("Phần trăm giảm giá không hợp lệ.");
        }

        // Lấy thời gian hiện tại và thời gian bắt đầu
        Instant now = Instant.now();
        Instant oneDayBeforeNow = now.minus(1, ChronoUnit.DAYS); // Cho phép ngày bắt đầu là 1 ngày trước

        // Kiểm tra ngày bắt đầu: phải là hôm qua, hôm nay, hoặc tương lai
        if (discountModel.getStartDate().isBefore(oneDayBeforeNow)) {
            throw new IllegalArgumentException("Ngày bắt đầu không thể là quá khứ xa hơn 1 ngày.");
        }

        // Kiểm tra ngày kết thúc phải sau ngày bắt đầu
        if (discountModel.getEndDate().isBefore(discountModel.getStartDate())) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu.");
        }

        // Kiểm tra khoảng cách ít nhất là 24 giờ (bao gồm giờ, phút, giây)
        long secondsBetween = ChronoUnit.SECONDS.between(discountModel.getStartDate(), discountModel.getEndDate());
        if (secondsBetween < 86400) { // 86400 giây = 24 giờ
            throw new IllegalArgumentException("Khoảng thời gian giữa ngày bắt đầu và ngày kết thúc phải ít nhất 24 giờ.");
        }

        // Kiểm tra nếu ngày bắt đầu và ngày kết thúc là cùng ngày
        LocalDate startDateOnly = discountModel.getStartDate().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDateOnly = discountModel.getEndDate().atZone(ZoneId.systemDefault()).toLocalDate();

        if (startDateOnly.equals(endDateOnly)) {
            // Nếu ngày trùng, kiểm tra giờ, phút, và giây
            int startHour = discountModel.getStartDate().atZone(ZoneId.systemDefault()).getHour();
            int endHour = discountModel.getEndDate().atZone(ZoneId.systemDefault()).getHour();

            int startMinute = discountModel.getStartDate().atZone(ZoneId.systemDefault()).getMinute();
            int endMinute = discountModel.getEndDate().atZone(ZoneId.systemDefault()).getMinute();

            int startSecond = discountModel.getStartDate().atZone(ZoneId.systemDefault()).getSecond();
            int endSecond = discountModel.getEndDate().atZone(ZoneId.systemDefault()).getSecond();

            // Kiểm tra nếu giờ/phút/giây của ngày bắt đầu trùng với ngày kết thúc
            if (startHour == endHour && startMinute == endMinute && startSecond == endSecond) {
                throw new IllegalArgumentException("Giờ/phút/giây của ngày bắt đầu không được trùng với ngày kết thúc.");
            }
        }

        // Kiểm tra loại phòng tồn tại
        TypeRoom typeRoom = typeRoomRepository.findById(discountModel.getTypeRoomId())
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy loại phòng với ID: " + discountModel.getTypeRoomId()));

        // Cập nhật thông tin giảm giá
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
