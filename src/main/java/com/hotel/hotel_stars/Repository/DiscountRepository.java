package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.Discount;
import com.hotel.hotel_stars.Entity.TypeRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    Discount findByDiscountName(String discountName);

    @Query("SELECT d FROM Discount d WHERE d.typeRoom = :typeRoom")
    List<Discount> findByRoomTypeId(@Param("typeRoom") TypeRoom typeRoom);
    @Query(value = "SELECT * FROM discount WHERE NOW() BETWEEN start_date AND end_date AND type_room_id = :typeRoomId", nativeQuery = true)
    List<Discount> findActiveDiscountsForTypeRoom(@Param("typeRoomId") Integer typeRoomId);

    @Query(value = "SELECT ds.* " +
            "FROM discount ds " +
            "JOIN discount_account da ON ds.id = da.discount_id " +
            "JOIN accounts ac ON ac.id = da.account_id " +
            "WHERE ac.username = :username " +
            "AND CURDATE() BETWEEN DATE(ds.start_date) AND DATE(ds.end_date) and da.status_da=0",
            nativeQuery = true)
    List<Discount> findDiscountsByUsername(@Param("username") String username);

    @Query(value = "SELECT * " +
            "FROM discount ds " +
            "WHERE ds.id NOT IN  (SELECT da.discount_id FROM discount_account da WHERE da.account_id = :id_account) " +
            "AND CONVERT_TZ(:currentTime, '+00:00', '+07:00') BETWEEN ds.start_date AND ds.end_date AND ds.status = 1;",
            nativeQuery = true)
    Discount findDiscountsByDate(@Param("currentTime") String currentTime, @Param("id_account") Integer id_account);
    
    @Query("select d from Discount d where d.discountName = ?1")
    List<Discount> findByDiscountNames(String discountName);


    @Query(value = "SELECT ds.id, ds.discount_name, ds.percent, ds.start_date, ds.end_date, da.status_da " +
            "FROM discount ds " +
            "JOIN discount_account da ON ds.id = da.discount_id " +
            "WHERE da.account_id = :id_account;",
            nativeQuery = true)
    List<Object[]> findDiscountsByAccount(@Param("id_account") Integer id_account);
}