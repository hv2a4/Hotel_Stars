package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.DTO.selectDTO.FindTypeRoomDto;
import com.hotel.hotel_stars.Entity.TypeRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TypeRoomRepository extends JpaRepository<TypeRoom, Integer> {

    @Query(value = "SELECT tr.type_room_name, tr.acreage, tr.capacity, " +
            "GROUP_CONCAT(atr.amenities_type_room_name) AS amenitiesTypeRoomNames, " +
            "(TIMESTAMPDIFF(DAY, :startDate, :endDate) * tr.price) AS estCost " +
            "FROM type_room tr " +
            "JOIN type_room_amenities_type_room trat ON tr.id = trat.type_room_id " +
            "JOIN amenities_type_room atr ON trat.amenities_type_room_id = atr.id " +
            "GROUP BY tr.id",
            nativeQuery = true)
    List<Object[]> findAllTypeRoomDetailsWithCost(@Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);




}