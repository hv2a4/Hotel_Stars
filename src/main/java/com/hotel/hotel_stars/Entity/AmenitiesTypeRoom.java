package com.hotel.hotel_stars.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "amenities_type_room", schema = "hotel_manager")
public class AmenitiesTypeRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "amenities_type_room_name")
    private String amenitiesTypeRoomName;

    @OneToMany(mappedBy = "amenitiesTypeRoom")
    List<TypeRoomAmenitiesTypeRoom > typeRoomAmenitiesTypeRoomList;

}