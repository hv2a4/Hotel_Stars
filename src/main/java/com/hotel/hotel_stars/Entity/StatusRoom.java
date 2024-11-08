package com.hotel.hotel_stars.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "status_room", schema = "hotel_manager")
public class StatusRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "status_room_name")
    private String statusRoomName;

    @OneToMany(mappedBy = "statusRoom")
    private List<Room> rooms;
}