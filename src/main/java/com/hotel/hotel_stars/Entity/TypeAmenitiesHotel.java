package com.hotel.hotel_stars.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "type_amenities_hotel", schema = "hotel_manager")
public class TypeAmenitiesHotel {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "type_amenities_hotel_name")
    private String typeAmenitiesHotelName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(mappedBy = "typeAmenitiesHotel")
    List<AmenitiesHotel> amenitiesHotels;

}