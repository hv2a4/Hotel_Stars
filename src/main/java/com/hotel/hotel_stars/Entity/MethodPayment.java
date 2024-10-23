package com.hotel.hotel_stars.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.awt.print.Book;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "method_payment", schema = "hotel_manager")
public class MethodPayment {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "method_payment_name")
    private String methodPaymentName;

    @OneToMany(mappedBy = "methodPayment")
    List<Booking> bookingList;
}