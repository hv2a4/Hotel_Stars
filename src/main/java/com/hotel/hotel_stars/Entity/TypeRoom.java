package com.hotel.hotel_stars.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "type_room", schema = "hotel_manager")
public class TypeRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "type_room_name")
    private String typeRoomName;

    @Column(name = "price")
    private Double price;

    @Column(name = "bed_count")
    private Integer bedCount;

    @Column(name = "acreage")
    private Double acreage;

    @Column(name = "guest_limit")
    private Integer guestLimit;

    @Column(name = "describes")
    private String describes;

    @OneToMany(mappedBy = "typeRoom")
    List<TypeRoomImage> typeRoomImages;

    @OneToMany(mappedBy = "typeRoom")
    List<Discount> discountList;

    @OneToMany(mappedBy = "typeRoom")
    List<TypeRoomAmenitiesTypeRoom> typeRoomAmenitiesTypeRoomList;

    @OneToMany(mappedBy = "typeRoom")
    List<TypeRoomServicePackage> typeRoomServicePackageList;

    @OneToMany(mappedBy = "typeRoom")
    List<Room> roomList;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_bed_id")
    private TypeBed typeBed;

}