package com.hotel.hotel_stars.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "type_bed", schema = "hotel_manager")
public class TypeBed {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "bed_name")
    private String bedName;

    @OneToMany(mappedBy = "typeBed")
    private List<TypeRoom> typeRooms;
}