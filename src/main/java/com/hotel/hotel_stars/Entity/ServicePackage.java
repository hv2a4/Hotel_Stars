package com.hotel.hotel_stars.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "service_package", schema = "hotel_manager")
public class ServicePackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "service_package_name")
    private String servicePackageName;

    @Column(name = "price")
    private Double price;

    @OneToMany(mappedBy = "servicePackage")
    private List<TypeRoomServicePackage> roomServicePackageList;
}