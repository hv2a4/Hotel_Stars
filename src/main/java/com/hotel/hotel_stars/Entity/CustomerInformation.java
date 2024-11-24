package com.hotel.hotel_stars.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "customer_information", schema = "hotel_manager")
public class CustomerInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "cccd")
    private String cccd;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "phone", length = 10)
    private String phone;

    @ColumnDefault("b'1'")
    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "birthday")
    private Instant birthday;

    @Column(name = "img_first_card")
    private String imgFirstCard;

    @Column(name = "img_last_card")
    private String imgLastCard;

    @OneToMany(mappedBy = "customerInformation")
    List<BookingRoomCustomerInformation> customerInformationList;
}