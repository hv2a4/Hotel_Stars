package com.hotel.hotel_stars.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hotel.hotel_stars.DTO.Select.AccountRoleDTOs;
import com.hotel.hotel_stars.Entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUsername(String userName);
    @Query(value = """
                SELECT 
                    a.username AS Username,
                    a.fullname AS FullName,
                    a.phone AS PhoneNumber,
                    a.email AS Email,
                    r.role_name AS Role,
                    bh.service_hotel_name AS ServiceName,
                    b.create_at AS BookingCreationDate,
                    a.avatar AS Avt,
                    a.gender AS Gender,
                    a.id as id
                FROM 
                    accounts a
                JOIN 
                    roles r ON a.role_id = r.id
                LEFT JOIN 
                    booking b ON a.id = b.account_id
                LEFT JOIN 
                    booking_room_service_hotel brsh ON b.id = brsh.booking_room_id
                LEFT JOIN 
                    service_hotel bh ON brsh.service_hotel_id = bh.id
            """, nativeQuery = true)
    List<Object[]> findAccountBookings();

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<Account> findByEmail(String email);
    @EntityGraph(attributePaths = {"bookingList"})
    List<Account> findAll();

    @Query("SELECT new com.hotel.hotel_stars.DTO.Select.AccountRoleDTOs(a.id, a.fullname, a.avatar, r.roleName) " +
            "FROM Account a JOIN a.role r " +
            "WHERE r.roleName = 'Staff'")
    List<AccountRoleDTOs> findAccountsWithRoleStaff();
}