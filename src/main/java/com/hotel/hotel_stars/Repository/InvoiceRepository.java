package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
}