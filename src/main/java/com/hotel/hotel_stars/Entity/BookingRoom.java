package com.hotel.hotel_stars.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "booking_room", schema = "hotel_manager")
public class BookingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "check_in")
    private Instant checkIn;

    @Column(name = "check_out")
    private Instant checkOut;

    @Column(name = "price")
    private Double price;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "bookingRoom")
    List<BookingRoomServiceRoom> serviceRooms;

    @OneToMany(mappedBy = "bookingRoom")
    List<BookingRoomCustomerInformation> customerInformationList;

    @OneToMany(mappedBy = "bookingRoom")
    List<BookingRoomServiceRoom> serviceRoomList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_staff")
    private Account account;

    @Override
    public String toString() {
        return "BookingRoom{" +
                "room=" + (room != null ? room.getId() : "null") + ", " + // or any meaningful property
                "price=" + price +
                '}';
    }
}