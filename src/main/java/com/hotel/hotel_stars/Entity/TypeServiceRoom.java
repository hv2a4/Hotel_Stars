package com.hotel.hotel_stars.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "type_service_room", schema = "hotel_manager")
public class TypeServiceRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "service_room_name")
    private String serviceRoomName;

    @Column(name = "duration")
    String duration;

    @OneToMany(mappedBy = "typeServiceRoomId", fetch=FetchType.LAZY)
    private Set<ServiceRoom> serviceRooms = new LinkedHashSet<>();
}