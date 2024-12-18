package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query(value = "SELECT fb.id, fb.stars, fb.create_at, fb.content, ac.fullname, ac.email " +
            "FROM feedback fb " +
            "JOIN invoice iv ON fb.invoice_id = iv.id " +
            "JOIN booking bk ON iv.booking_id = bk.id " +
            "JOIN accounts ac ON bk.account_id = ac.id " +
            "WHERE fb.rating_status = 1;",
            nativeQuery = true)
    List<Object[]> getAll();
}
