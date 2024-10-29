package com.hotel.hotel_stars.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "service_hotel", schema = "hotel_manager")
public class ServiceHotel {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "service_hotel_name")
    private String serviceHotelName;

    @Column(name = "price")
    private Double price;

    @Column(name = "image")
    private String imageName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_service_hotel_id")
    private TypeServiceHotel typeServiceHotel;

    @OneToMany(mappedBy = "serviceHotel")
    private List<BookingRoomServiceHotel> serviceHotelList;
}