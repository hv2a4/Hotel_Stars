package com.hotel.hotel_stars.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "discount", schema = "hotel_manager")
public class Discount {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "discount_name")
    private String discountName;

    @Column(name = "percent")
    private Double percent;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_room_id")
    private TypeRoom typeRoom;

}