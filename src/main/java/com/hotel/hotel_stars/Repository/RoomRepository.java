package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.DTO.Select.RoomInfoDTO;
import com.hotel.hotel_stars.Entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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
            "type_room.guest_limit, " +
            "type_room.acreage, " +
            "type_room_image.id " +
            "FROM room " +
            "JOIN type_room ON room.type_room_id = type_room.id " +
            "JOIN type_bed ON type_room.type_bed_id = type_bed.id " +
            "JOIN type_room_image ON type_room.id = type_room_image.type_room_id " +
            "GROUP BY type_room.id, type_room.type_room_name, type_room.price, " +
            "type_bed.id, type_room.guest_limit, type_room.acreage, type_room_image.id",
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


}