package com.hotel.hotel_stars.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "type_service_hotel", schema = "hotel_manager")
public class TypeServiceHotel {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "type_service_hotel_name")
    private String typeServiceHotelName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(mappedBy = "typeServiceHotel")
    List<ServiceHotel> serviceHotels;

}