package com.hotel.hotel_stars.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "hotel", schema = "hotel_manager")
public class Hotel {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "hotel_name")
    private String hotelName;

    @Column(name = "descriptions")
    private String descriptions;

    @Column(name = "province")
    private String province;

    @Column(name = "district")
    private String district;

    @Column(name = "ward")
    private String ward;

    @Column(name = "address")
    private String address;

//    @Column(name = "describes")
//    private String describes;

    @Column(name = "hotel_phone")
    private String hotelPhone;

    @OneToMany(mappedBy = "hotel")
    private List<HotelImage> hotelImageList;

    @OneToMany(mappedBy = "hotel")
    private List<AmenitiesHotel> amenitiesHotelList;

    @OneToMany(mappedBy = "hotel")
    private List<ServiceHotel> serviceHotelList;
}