package com.hotel.hotel_stars.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "accounts", schema = "hotel_manager")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "passwords")
    private String passwords;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "phone")
    private String phone;

    @Column(name = "is_delete")
    private Boolean isDelete;

    @Column(name = "email")
    private String email;

    @Column(name = "avatar")
    private String avatar;

    @ColumnDefault("b'1'")
    @Column(name = "gender")
    private Boolean gender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "account")
    List<Booking> bookingList;

    @OneToMany(mappedBy = "account")
    List<BookingRoom> bookingRooms;

    @OneToMany(mappedBy = "account")
    List<DiscountAccount> discountAccounts;
}