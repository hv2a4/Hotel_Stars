package com.hotel.hotel_stars.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private LocalDateTime createAt;

    @Column(name = "start_at")
    private Instant startAt;


    @Column(name = "end_at")
    private Instant endAt;

    @Column(name = "status_payment")
    private Boolean statusPayment;

    @Column(name = "discount_name")
    private String discountName;

    @Column(name = "discount_percent")
    private Double discountPercent;

    @Column(name = "descriptions")
    private String descriptions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "method_payment_id")
    private MethodPayment methodPayment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private StatusBooking status;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    List<BookingRoom> bookingRooms  = new ArrayList<>();
    
    @OneToMany(mappedBy = "booking")
    @JsonManagedReference
    List<Invoice> invoice;

    @OneToMany(mappedBy = "booking")
    List<Invoice> invoices;


}