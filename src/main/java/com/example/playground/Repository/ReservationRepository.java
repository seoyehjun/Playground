package com.example.playground.Repository;

import com.example.playground.Model.reserveEntity.Reservation;
import com.example.playground.Model.reserveEntity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;

public interface ReservationRepository extends JpaRepository<Reservation, Integer>
{
    boolean existsByRoomAndStartDayLessThanEqualAndEndDayGreaterThanEqual(Room room, Date endday, Date startday);
}
