package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedBackRepository extends JpaRepository <Feedback, Integer> {
    @Query(value = "SELECT a.fullname, f.content, a.avatar " +
            "FROM accounts a " +
            "JOIN booking b ON a.id = b.account_id " +
            "JOIN invoice i ON b.id = i.booking_id " +
            "JOIN feedback f ON i.id = f.invoice_id " +
            "WHERE a.role_id = 3 and f.stars >= 4 ",
            nativeQuery = true)
    List<Object[]> findFeedbacksByRoleIdNative();
}
