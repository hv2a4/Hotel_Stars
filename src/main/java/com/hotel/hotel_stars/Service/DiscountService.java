package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.DiscountDto;
import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.Entity.Discount;
import com.hotel.hotel_stars.Entity.TypeRoom;
import com.hotel.hotel_stars.Models.DiscountModel;
import com.hotel.hotel_stars.Repository.DiscountAccountRepository;
import com.hotel.hotel_stars.Repository.DiscountRepository;
import com.hotel.hotel_stars.Repository.TypeRoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
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
    @Autowired
    DiscountAccountRepository discountAccountRepository;

    public List<DiscountDto> getDiscountByAccount(String userName) {
        List<Discount> discounts = discountRepository.findDiscountsByUsername(userName);
        if (discounts == null || discounts.isEmpty()) {
            return null;
        }
        return discounts.stream().map(this::convertToDto).toList();
    }

    public DiscountDto getDiscountByDate(Integer id_account) {
        Instant now = Instant.now();
        ZonedDateTime zonedNow = ZonedDateTime.ofInstant(now, ZoneOffset.UTC);
        String currentTime = zonedNow.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);

        Discount discount = discountRepository.findDiscountsByDate(currentTime, id_account);
        if (discount == null) {
            return null;
        }
        return convertToDto(discount);
    }

    public List<Object[]> getDiscountByAccount(Integer id_account) {
        List<Object[]> results = discountRepository.findDiscountsByAccount(id_account);

        if (results == null || results.isEmpty()) {
            return null;
        }

        return results;
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
        // Kiểm tra null cho discountModel
        if (discountModel == null) {
            return new StatusResponseDto("400", "FAILURE", "Dữ liệu giảm giá không được để trống.");
        }

        // Lấy ngày hiện tại (chỉ ngày, không bao gồm giờ)
        LocalDate today = LocalDate.now();

        // Kiểm tra thời gian bắt đầu và kết thúc
        if (discountModel.getStartDate() == null || discountModel.getEndDate() == null) {
            return new StatusResponseDto("400", "FAILURE", "Ngày bắt đầu và ngày kết thúc không được để trống.");
        }

        // Chuyển đổi Instant sang LocalDate để so sánh
        LocalDate startDate = discountModel.getStartDate()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate endDate = discountModel.getEndDate()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // Kiểm tra ngày bắt đầu không ở quá khứ
        if (startDate.isBefore(today)) {
            return new StatusResponseDto("400", "FAILURE", "Ngày bắt đầu không được ở trong quá khứ.");
        }

        // Kiểm tra phần trăm giảm giá
        if (discountModel.getPercent() == null || discountModel.getPercent() <= 0 || discountModel.getPercent() > 100) {
            return new StatusResponseDto("400", "FAILURE", "Phần trăm giảm giá phải lớn hơn 0 và không vượt quá 100.");
        }

        Instant start_date = discountModel.getStartDate();
        Instant end_date = discountModel.getEndDate();

        // Kiểm tra ngày kết thúc
        if (end_date.isBefore(start_date)) {
            return new StatusResponseDto("400", "FAILURE", "Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu.");
        }


        // Kiểm tra thời gian trùng lặp với giảm giá khác
        List<Discount> existingDiscounts = discountRepository.findAll();
        for (Discount existingDiscount : existingDiscounts) {
            Instant existingStartDate = existingDiscount.getStartDate();
            Instant existingEndDate = existingDiscount.getEndDate();

            // Kiểm tra trùng lặp thời gian
            if (!(end_date.isBefore(existingStartDate) || start_date.isAfter(existingEndDate))) {
                // Nếu hai khoảng thời gian không hoàn toàn tách biệt
                return new StatusResponseDto("400", "FAILURE",
                        String.format("Khoảng thời gian giảm giá bị trùng lặp với giảm giá khác: %s",
                                existingDiscount.getDiscountName()
                        )
                );
            }
        }

        // Nếu tất cả điều kiện hợp lệ, tiếp tục lưu dữ liệu
        try {
            Discount discount = new Discount();
            discount.setDiscountName(discountModel.getDiscountName());
            discount.setPercent(discountModel.getPercent());
            discount.setStartDate(discountModel.getStartDate());
            discount.setEndDate(discountModel.getEndDate());
            discount.setStatus(true);
            discountRepository.save(discount);
            return new StatusResponseDto("200", "SUCCESS", "Thêm giảm giá thành công.");
        } catch (Exception e) {
            return new StatusResponseDto("500", "FAILURE", "Lỗi trong quá trình thêm giảm giá: " + e.getMessage());
        }
    }


    public DiscountDto updateDiscountDto(DiscountModel discountModel) {
        // Kiểm tra xem giảm giá có tồn tại không
        Optional<Discount> optionalDiscount = discountRepository.findById(discountModel.getId());
        if (optionalDiscount.isEmpty()) {
            throw new NoSuchElementException("Không tìm thấy giảm giá với ID: " + discountModel.getId());
        }

        Discount discount = optionalDiscount.get();

        // Lấy ngày hiện tại (không xét giờ)
        LocalDate today = LocalDate.now();

        // Kiểm tra thời gian bắt đầu và kết thúc
        if (discountModel.getStartDate() == null || discountModel.getEndDate() == null) {
            throw new IllegalArgumentException("Ngày bắt đầu và ngày kết thúc không được để trống.");
        }

        // Chuyển đổi Instant sang LocalDate để kiểm tra
        LocalDate startDate = discountModel.getStartDate()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate endDate = discountModel.getEndDate()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // Bỏ qua kiểm tra ngày nếu thời gian giảm giá không thay đổi
        boolean isDateUnchanged = startDate.equals(discount.getStartDate()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()) &&
                endDate.equals(discount.getEndDate()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate());

        if (!isDateUnchanged) {
            // Kiểm tra ngày bắt đầu không ở trong quá khứ
            if (startDate.isBefore(today)) {
                throw new IllegalArgumentException("Ngày bắt đầu không được ở trong quá khứ.");
            }
            Instant start_date = discountModel.getStartDate();
            Instant end_date = discountModel.getEndDate();
            // Kiểm tra ngày kết thúc phải sau hoặc bằng ngày bắt đầu
            if (end_date.isBefore(start_date)) {
                throw new IllegalArgumentException("Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu.");
            }

            // Kiểm tra thời gian trùng lặp với các giảm giá khác
            List<Discount> existingDiscounts = discountRepository.findAll();
            for (Discount existingDiscount : existingDiscounts) {
                if (!existingDiscount.getId().equals(discountModel.getId())) { // Bỏ qua chính giảm giá đang cập nhật
                    LocalDate otherStartDate = existingDiscount.getStartDate()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    LocalDate otherEndDate = existingDiscount.getEndDate()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    if (!(endDate.isBefore(otherStartDate) || startDate.isAfter(otherEndDate))) {
                        // Nếu hai khoảng thời gian không hoàn toàn tách biệt
                        throw new IllegalArgumentException(String.format(
                                "Khoảng thời gian giảm giá bị trùng lặp với giảm giá khác: %s",
                                existingDiscount.getDiscountName()
                        ));
                    }
                }
            }
        }

        // Kiểm tra phần trăm giảm giá
        if (discountModel.getPercent() == null || discountModel.getPercent() <= 0 || discountModel.getPercent() > 100) {
            throw new IllegalArgumentException("Phần trăm giảm giá phải lớn hơn 0 và không vượt quá 100.");
        }

        // Nếu tất cả điều kiện hợp lệ, tiếp tục cập nhật thông tin giảm giá
        discount.setDiscountName(discountModel.getDiscountName());
        discount.setPercent(discountModel.getPercent());
        discount.setStartDate(discountModel.getStartDate());
        discount.setEndDate(discountModel.getEndDate());

        discountRepository.save(discount);
        return convertToDto(discount);
    }

    public StatusResponseDto hidenDiscount(Integer discountId) {
        Optional<Discount> optionalDiscount = discountRepository.findById(discountId);

        if (optionalDiscount.isPresent()) {
            Discount discount = optionalDiscount.get();
            discount.setStatus(false); // Cập nhật trạng thái giảm giá
            discountRepository.save(discount);

            return new StatusResponseDto("200", "SUCCESS", "Ẩn giảm giá thành công.");
        }

        return new StatusResponseDto("404", "FAILURE", "Không tìm thấy giảm giá với ID: " + discountId);
    }

    public StatusResponseDto showDiscount(Integer discountId) {
        Optional<Discount> optionalDiscount = discountRepository.findById(discountId);

        if (optionalDiscount.isPresent()) {
            Discount discount = optionalDiscount.get();
            discount.setStatus(true); // Cập nhật trạng thái giảm giá
            discountRepository.save(discount);

            return new StatusResponseDto("200", "SUCCESS", "Hiện giảm giá thành công.");
        }

        return new StatusResponseDto("404", "FAILURE", "Không tìm thấy giảm giá với ID: " + discountId);
    }




    public StatusResponseDto deletById(Integer id) {
        // Kiểm tra xem giảm giá có tồn tại trước khi xóa
        if (!discountRepository.existsById(id)) {
            return new StatusResponseDto("404", "FAILURE", "Không tìm thấy giảm giá với ID: " + id);
        }

        try {
            discountRepository.deleteById(id);
            return new StatusResponseDto("200", "SUCCESS", "Xóa giảm giá thành công!");
        } catch (DataIntegrityViolationException e) {
            // Bắt lỗi ràng buộc khóa ngoại
            return new StatusResponseDto("409", "FAILURE", "Không thể xóa giảm giá vì đang được khách hàng sử dụng.");
        } catch (Exception e) {
            // Xử lý lỗi chung
            return new StatusResponseDto("500", "FAILURE", "Lỗi trong quá trình xóa giảm giá.");
        }
    }


    public List<DiscountDto> getDiscountsByName(String discountName) {
        List<Discount> discount = discountRepository.findByDiscountNames(discountName);
        return discount.stream().map(this::convertToDto).toList();
    }

}
