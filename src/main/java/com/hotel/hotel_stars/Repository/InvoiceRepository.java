package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.Invoice;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
	Optional<Invoice> findByBooking_Id(Integer bookingId);
}