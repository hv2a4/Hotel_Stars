package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.BookingRoomCustomerInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRoomCustomerInformationRepository extends JpaRepository<BookingRoomCustomerInformation, Integer> {
}