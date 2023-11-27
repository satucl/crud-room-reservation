package com.example.SimplestCRUDExample.controller;

import com.example.SimplestCRUDExample.model.Reservation;
import com.example.SimplestCRUDExample.repo.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ReservationController {

    @Autowired
    ReservationRepository reservationRepository;

    @PostMapping("/cancelReservation")
    public ResponseEntity<String> cancelReservation(@RequestBody String resNumber) {
        try
        {
            if (resNumber == null || resNumber.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Reservation res = reservationRepository.findByResNumber(resNumber);
            if (res == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            res.setCancelled(true);
            reservationRepository.save(res);
            return new ResponseEntity<>(res.getResNumber() + " successfully cancelled", HttpStatus.OK);


        }
        catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addReservation")
    public ResponseEntity<String> addReservation(@RequestBody Reservation reservation) {
        try {
            reservation.setCancelled(false);
            reservation.setResNumber(generateReservationCode());
            reservationRepository.save(reservation);
            return new ResponseEntity<>(reservation.getResNumber(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String generateReservationCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789";
        return new Random().ints(6, 0, chars.length())
                .mapToObj(i -> String.valueOf(chars.charAt(i)))
                .collect(Collectors.joining());
    }

    @GetMapping("/getAllReservations")
    public ResponseEntity<List<Reservation>> getAllReservations(@RequestParam(required = false) String checkInFrom,
                                                                @RequestParam(required = false) String checkInTo) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            List<Reservation> reservationList = new ArrayList<>();

            // checkin and checkout
            if (checkInFrom != null && checkInTo != null) {
                reservationList = reservationRepository.findByCheckInBetween(sdf.parse(checkInFrom),
                        sdf.parse(checkInTo));
            }
            else if (checkInFrom != null) {
                reservationList = reservationRepository.findByCheckInAfter(sdf.parse(checkInFrom));
            }
            else if (checkInTo != null) {
                reservationList = reservationRepository.findByCheckInBefore(sdf.parse(checkInTo));
            }
            else {
                reservationRepository.findAll().forEach(reservationList::add);
            }

            if (reservationList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(reservationList, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
