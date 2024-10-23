package com.hotel.hotel_stars.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "rating", schema = "hotel_manager")
public class Rating {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "content")
    private String content;

    @Column(name = "stars")
    private Integer stars;

    @Column(name = "create_at")
    private Instant createAt;

    @ColumnDefault("b'1'")
    @Column(name = "rating_status")
    private Boolean ratingStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

}