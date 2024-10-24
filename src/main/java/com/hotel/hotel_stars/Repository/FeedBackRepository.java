package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.Discount;
import com.hotel.hotel_stars.Entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedBackRepository extends JpaRepository <Feedback, Integer> {

}
