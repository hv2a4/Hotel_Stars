package com.hotel.hotel_stars.DTO.Select;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponseDto<T> {
	private List<T> content;
	private int currentPage;
	private int totalPages;
	private long totalItems;
}
