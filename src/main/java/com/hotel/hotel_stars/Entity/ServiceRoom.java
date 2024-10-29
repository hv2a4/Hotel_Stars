package com.hotel.hotel_stars.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "service_room", schema = "hotel_manager")
public class ServiceRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "service_room_name")
    private String serviceRoomName;

    @Column(name = "price")
    private Double price;

    @OneToMany(mappedBy = "serviceRoom")
    private List<BookingRoomServiceRoom> bookingRoomServiceRooms;

}