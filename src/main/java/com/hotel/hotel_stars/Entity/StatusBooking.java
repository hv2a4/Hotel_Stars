package com.hotel.hotel_stars.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "status_booking", schema = "hotel_manager")
public class StatusBooking {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "status_booking_name")
    private String statusBookingName;

    @OneToMany(mappedBy = "status")
    List<Booking> bookingList;
}