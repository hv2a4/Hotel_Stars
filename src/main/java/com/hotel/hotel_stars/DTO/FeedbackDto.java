package com.hotel.hotel_stars.DTO;

import com.hotel.hotel_stars.Entity.Feedback;
import com.hotel.hotel_stars.Entity.Invoice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link Feedback}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDto implements Serializable {
    Integer id;
    String content;
    Integer stars;
    Instant createAt;
    Boolean ratingStatus;
    InvoiceDto invoiceDto;
}