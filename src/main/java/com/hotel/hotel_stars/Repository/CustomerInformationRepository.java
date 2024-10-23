package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.CustomerInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerInformationRepository extends JpaRepository<CustomerInformation, Integer> {
}