package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.DTO.Select.RoomInfoDTO;
import com.hotel.hotel_stars.Entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface RoomRepository extends JpaRepository<Room, Integer> {
    @Query(value = "SELECT " +
            "(SELECT COUNT(*) FROM accounts WHERE role_id = 2) AS count_employees, " +
            "(SELECT COUNT(*) FROM accounts WHERE role_id = 3) AS count_customers, " +
            "(SELECT COUNT(ro.id) FROM floors fl JOIN room ro ON fl.id = ro.floor_id) AS total_room " +
            "FROM accounts " +
            "GROUP BY count_employees, count_customers", nativeQuery = true)
    List<Object[]> getCounts();

    @Query(value = "SELECT type_room.id AS type_room_id, " +
            "type_room.type_room_name, " +
            "COUNT(room.id) AS room_count, " +
            "type_room.price, " +
            "type_bed.id AS type_bed, " +
            "type_room.bed_count, " +
            "type_room.guest_limit, " +
            "type_room.acreage, " +
            "type_room_image.id " +
            "FROM room " +
            "JOIN type_room ON room.type_room_id = type_room.id " +
            "JOIN type_bed ON type_room.type_bed_id = type_bed.id " +
            "JOIN type_room_image ON type_room.id = type_room_image.type_room_id " +
            "GROUP BY type_room.id, type_room.type_room_name, type_room.price, " +
            "type_bed.id, type_room.bed_count, type_room.guest_limit, type_room.acreage, type_room_image.id",
            nativeQuery = true)
    List<Object[]> getRoomTypeData();

    List<Room> findByTypeRoomId(Integer typeRoomId);

    @Query(value = """
                SELECT 
                    r.roomName,
                    tr.typeRoomName,
                    f.floorName,
                    sr.statusRoomName,
                    r.id,
                    tr.id,
                    sr.id
                FROM Room r
                JOIN r.typeRoom tr
                JOIN r.floor f
                JOIN r.statusRoom sr
            """)
    List<Object[]> findAllRoomInfo();

    @Query(value = """
        SELECT 
            room.room_name, 
            type_room.type_room_name, 
            booking_room.check_in, 
            booking_room.check_out, 
            accounts.username AS guest_name, 
            status_room.status_room_name 
        FROM 
            type_room 
            JOIN type_room_image ON type_room.id = type_room_image.type_room_id 
            JOIN room ON type_room.id = room.type_room_id 
            JOIN status_room ON room.status_room_id = status_room.id 
            JOIN booking_room ON room.id = booking_room.room_id 
            JOIN booking ON booking_room.booking_id = booking.id 
            JOIN accounts ON booking.account_id = accounts.id 
        WHERE 
            type_room.id = ?1 
        ORDER BY 
            booking_room.check_in ASC
        """, nativeQuery = true)
    List<Object[]> findBookingsByTypeRoomIdOrderedByCheckIn(int typeRoomId);
    @Query("select r from Room r where r.floor.id = ?1")
    List<Room> findByFloorId(Integer floorId);
    
    
    Page<Room> findAll(Pageable pageable);

}