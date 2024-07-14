package com.example.playground.Repository;

import com.example.playground.Model.reserveEntity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room,Integer>
{
    Optional<Room> findByRoomId(Long roomId);
}
