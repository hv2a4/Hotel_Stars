package com.hotel.hotel_stars.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "type_room_amenities_type_room", schema = "hotel_manager")
public class TypeRoomAmenitiesTypeRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_room_id")
    private TypeRoom typeRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amenities_type_room_id")
    private AmenitiesTypeRoom amenitiesTypeRoom;

}