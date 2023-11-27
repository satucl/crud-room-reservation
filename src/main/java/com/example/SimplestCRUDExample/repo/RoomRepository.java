package com.example.SimplestCRUDExample.repo;

import com.example.SimplestCRUDExample.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>
{
    List<Room>  findByAvailableFromBeforeAndAvailableToAfterAndBedsGreaterThanEqual(Date availableFrom, Date availableTo, int beds);
    List<Room>  findByAvailableFromBeforeAndAvailableToAfter(Date availableFrom, Date availableTo);
    List<Room>  findByBedsGreaterThanEqual(int beds);

}
