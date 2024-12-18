package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.BookingRoom;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BookingRoomRepository extends JpaRepository<BookingRoom, Integer> {
//	Optional<BookingRoom> findMostRecentBookingRoomByRoomIdAndStatusRoomId(Integer roomId, String statusRoomId);
	@Query("select br from BookingRoom br where br.booking.account.id = ?1")
	List<BookingRoom> findBookingRoomByAccountId(Integer id);
	List<BookingRoom> findByRoom_IdAndRoom_StatusRoom_Id(Integer roomId, Integer statusRoomId);
	List<BookingRoom> findByIdIn(List<Integer> ids);
	
	//khoi
	@Query(value = "SELECT br.* " +
            "FROM booking_room br " +
            "JOIN booking b ON br.booking_id = b.id " +
            "WHERE br.room_id = :roomId " +
            "AND b.status_id NOT IN (8, 6, 1, 3,5,9) " +
            "ORDER BY b.create_at ASC " +
            "LIMIT 1", 
    nativeQuery = true)
	BookingRoom findFirstBookingRoomByRoomIdAndStatusNotIn(@Param("roomId") Integer roomId);
	//khoi

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM booking_room WHERE booking_id = :bookingId", nativeQuery = true)
	Integer deleteByBookingId(Integer bookingId);

}