package com.example.SimplestCRUDExample.repo;

import com.example.SimplestCRUDExample.model.Reservation;
import com.example.SimplestCRUDExample.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByCheckInBetween(Date dateFrom, Date dateTo);
    List<Reservation> findByCheckInAfter(Date dateFrom);
    List<Reservation> findByCheckInBefore(Date dateTo);

    Reservation findByResNumber(String resNumber);
}
