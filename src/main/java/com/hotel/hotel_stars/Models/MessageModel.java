package com.hotel.hotel_stars.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MessageModel {
    @NotNull(message = "Không đuược bỏ trống id")
    Integer id;

    @NotBlank(message = "Không được bỏ trống lời tin nhắn")
    String message;

    @NotBlank(message = "Không được bỏ trống ngày tạo")
    String create_at;
}
