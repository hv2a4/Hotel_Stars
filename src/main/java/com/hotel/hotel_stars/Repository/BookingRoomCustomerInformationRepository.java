package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.BookingRoomCustomerInformation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookingRoomCustomerInformationRepository
		extends JpaRepository<BookingRoomCustomerInformation, Integer> {
	List<BookingRoomCustomerInformation> findByBookingRoom_IdIn(List<Integer> bookingRoomIds);
	BookingRoomCustomerInformation findByBookingRoomIdAndCustomerInformationId(Integer bookingRoomId, Integer customerInformationId);
}