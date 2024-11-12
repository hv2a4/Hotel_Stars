package com.hotel.hotel_stars.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "booking", schema = "hotel_manager")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "create_at")
    private Instant createAt;

    @Column(name = "start_at")
    private Instant startAt;

    @Column(name = "end_at")
    private Instant endAt;

    @Column(name = "status_payment")
    private Boolean statusPayment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "method_payment_id")
    private MethodPayment methodPayment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private StatusBooking status;

    @OneToMany(mappedBy = "booking")
    @JsonManagedReference
    List<BookingRoom> bookingRooms;

    @Override
    public String toString() {
        return "Booking{id=" + id + ", StDate=" + startAt + "}";
    }
}