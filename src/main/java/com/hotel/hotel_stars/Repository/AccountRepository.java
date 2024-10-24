package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUsername(String userName);
}