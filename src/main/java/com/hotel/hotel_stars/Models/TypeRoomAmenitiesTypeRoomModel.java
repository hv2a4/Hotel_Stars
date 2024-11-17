package com.hotel.hotel_stars.Models;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class TypeRoomAmenitiesTypeRoomModel {
    private Integer id;

    @NotNull(message = "Không được để trống ID")
    private Integer typeRoomId;

    @NotNull(message = "Không được để trống ID")
    private Integer amenitiesTypeRoomId;
}
