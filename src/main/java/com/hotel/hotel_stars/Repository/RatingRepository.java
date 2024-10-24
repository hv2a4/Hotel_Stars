package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Feedback, Integer> {
}