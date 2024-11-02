package com.hotel.hotel_stars.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "amenities_hotel", schema = "hotel_manager")
public class AmenitiesHotel {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "amenities_hotel_name")
    private String amenitiesHotelName;

    @Column(name = "icon")
    private String icon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

}