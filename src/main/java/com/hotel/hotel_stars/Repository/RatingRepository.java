package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
}