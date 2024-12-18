package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.DTO.InvoiceStatisticsDTO;
import com.hotel.hotel_stars.Entity.Invoice;

import java.time.Instant;
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
		
		@Query("SELECT DATE(i.createAt) AS bookingDate, "
		        + " SUM(i.totalAmount) AS totalRevenue, "
		        + " SUM(CASE WHEN i.invoiceStatus = FALSE THEN i.totalAmount ELSE 0 END) AS refundedAmount, "
		        + " SUM(i.totalAmount) - SUM(CASE WHEN i.invoiceStatus = FALSE THEN i.totalAmount ELSE 0 END) AS netRevenue "
		        + " FROM Invoice i WHERE i.createAt BETWEEN :startDate AND :endDate GROUP BY DATE(i.createAt)")
		List<Object[]> getRevenueByCondition(Instant startDate, Instant endDate);

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
		        GROUP BY
		            DATE(i.create_at)
		        ORDER BY
		            bookingDate
		    """, nativeQuery = true)
		List<Object[]> getInvoiceStatisticsByDateRange();
		@Query(value = """
		        SELECT
		            DATE(i.create_at) AS bookingDate,
		            COUNT(b.id) AS totalBookings,
		            COUNT(br.id) AS totalRoomsBooked,
		            SUM(i.total_amount) AS totalBookingValue,
		            SUM(CASE
		                WHEN b.status_payment = TRUE THEN i.total_amount
		                ELSE 0
		            END) AS totalPaid
		        FROM
		            booking b
		        INNER JOIN
		            invoice i ON b.id = i.booking_id
		        LEFT JOIN
		            booking_room br ON b.id = br.booking_id
		        GROUP BY
		            DATE(i.create_at)
		        ORDER BY
		            bookingDate
		    """, nativeQuery = true)
		List<Object[]> getBookingStatisticsByDateRange();


}