package com.hotel.hotel_stars.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "type_service_room", schema = "hotel_manager")
public class TypeServiceRoom {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "service_room_name")
    private String serviceRoomName;

    @OneToMany(mappedBy = "typeServiceRoomId")
    private List<ServiceRoom> serviceRooms;
}