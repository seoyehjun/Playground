//https://codechacha.com/ko/java8-stream-collect/<--스트림이란?
//https://s7won.tistory.com/5<--map()사용법
package com.example.playground.Service;

import com.example.playground.Model.Member;
import com.example.playground.Model.reserveEntity.Reservation;
import com.example.playground.Model.reserveEntity.Room;
import com.example.playground.Repository.ReservationRepository;
import com.example.playground.Repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository)
    {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    public Optional<Room> get_room(Long roomId)
    {
        return roomRepository.findByRoomId(roomId);
    }

    public List<Room> get_rooms()
    {
        return roomRepository.findAll();
    }

    public List<Reservation> get_reservations()
    {
        return reservationRepository.findAll();
    }

   /* public void making_reservation(Date startday, Date endday, Room room, Member member)
    {
        // 특정 날짜에 특정 방이 점유되어 있으면 예약을 거부
        if (reservationRepository.existsByRoomAndStartDayLessThanEqualAndEndDayGreaterThanEqual(room, endday, startday))
        {
            throw new RuntimeException("해당 기간에 이미 예약된 방입니다.");
            return;
        }

        Reservation reservation = new Reservation();
        reservation.setStartDay(startday);
        reservation.setEndDay(endday);
        reservation.setRoom(room);
        reservation.setMember(member);

        reservationRepository.save(reservation);
    }*/

    public String making_reservation(LocalDate startday, LocalDate endday, Room room, Member member)
    {
        String result;
        try
        {
            // 특정 날짜에 특정 방이 점유되어 있으면 예약을 거부
            if (reservationRepository.existsByRoomAndStartDayLessThanEqualAndEndDayGreaterThanEqual(room, endday, startday))
            {
                throw new RuntimeException("해당 기간에 이미 예약된 방입니다.");
            }

            Reservation reservation = new Reservation();
            reservation.setStartDay(startday);
            reservation.setEndDay(endday);
            reservation.setRoom(room);
            reservation.setMember(member);

            reservationRepository.save(reservation);

            result = "예약 성공";
        }
        catch (RuntimeException e)
        {
            System.out.println("예약 실패: " + e.getMessage());
            result ="예약 실패";
            //catch문안에서 바로 return을 안하는 이유는?
            //catch 블록 내에서 return 문을 사용하더라도 해당 메서드의 반환값은 catch 블록을 빠져나온 후에 결정됩니다.
            //return "예약 실패"이렇게 써놔도 아무것도 return 안된다.
        }

        return result;
    }


}
