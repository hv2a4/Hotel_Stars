package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.DTO.InvoiceStatisticsDTO;
import com.hotel.hotel_stars.Entity.Invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
	Optional<Invoice> findByBooking_Id(Integer bookingId);

	@Query(value = """
	        SELECT
	            SUM(i.total_amount) AS totalRevenue,
	            SUM(CASE 
	                WHEN i.invoice_status = FALSE THEN i.total_amount 
	                ELSE 0 
	            END) AS refundedAmount,
	            SUM(i.total_amount) - SUM(CASE 
	                WHEN i.invoice_status = FALSE THEN i.total_amount 
	                ELSE 0 
	            END) AS netRevenue
	        FROM
	            invoice i
	        WHERE
	            i.create_at BETWEEN :startDate AND :endDate
	        """, nativeQuery = true)
	List<Object[]> getTotalInvoiceStatistics(@Param("startDate") LocalDate startDate, 
	                                         @Param("endDate") LocalDate endDate);
	
	@Query(value = """
		    SELECT
		        DATE(i.create_at) AS bookingDate,     
		        SUM(i.total_amount) AS totalRevenue,  
		        SUM(CASE 
		            WHEN i.invoice_status = FALSE THEN i.total_amount 
		            ELSE 0 
		        END) AS refundedAmount,              
		        SUM(i.total_amount) - SUM(CASE 
		            WHEN i.invoice_status = FALSE THEN i.total_amount 
		            ELSE 0 
		        END) AS netRevenue                  
		    FROM
		        invoice i
		    WHERE
		        i.create_at BETWEEN :startDate AND :endDate
		    GROUP BY
		        DATE(i.create_at)
		    ORDER BY
		        bookingDate
		""", nativeQuery = true)
		List<Object[]> getInvoiceStatisticsByDateRange(@Param("startDate") LocalDate startDate,
		                                               @Param("endDate") LocalDate endDate);
}