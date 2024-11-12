package com.hotel.hotel_stars.Models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class serviceRoomModel {
    private Integer id;

    @NotBlank(message = "Tên dịch vụ phòng không được để trống")
    private String serviceRoomName;

    @NotNull(message = "Giá dịch vụ phòng không được để trống")
    @Positive(message = "Giá phải lớn hơn 0")
    private Double price;

    @NotBlank(message = "Vui lòng chọn hình ảnh")
    private String imageName;
    
    Integer typeServiceRoom;

//    // Phương thức kiểm tra tính hợp lệ của giá
//    public void validatePrice() {
//        if (price != null && (price <= 0 || !price.toString().matches("^[0-9]+(\\.[0-9]{1,2})?$"))) {
//            throw new IllegalArgumentException("Giá dịch vụ không hợp lệ");
//        }
//    }
}
