package com.example.SimplestCRUDExample.controller;

import com.example.SimplestCRUDExample.model.Room;
import com.example.SimplestCRUDExample.repo.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RoomController {

    @Autowired
    RoomRepository roomRepository;

    @GetMapping("/getAllRooms")
    public ResponseEntity<List<Room>> getAllRooms(@RequestParam(required = false) String dateFrom,
                                                  @RequestParam(required = false) String dateTo,
                                                  @RequestParam(required = false) String beds) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            List<Room> roomList = new ArrayList<>();

            if (dateFrom != null && dateTo != null && beds != null) {

                roomList = roomRepository.findByAvailableFromBeforeAndAvailableToAfterAndBedsGreaterThanEqual(sdf.parse(dateFrom),
                        sdf.parse(dateTo), Integer.parseInt(beds));
            }
            else if (dateFrom != null && dateTo != null) {
                roomList = roomRepository.findByAvailableFromBeforeAndAvailableToAfter(sdf.parse(dateFrom),
                        sdf.parse(dateTo));
            }
            else if (beds != null) {
                roomList = roomRepository.findByBedsGreaterThanEqual(Integer.parseInt(beds));
            }
            else {
                roomRepository.findAll().forEach(roomList::add);
            }

            if (roomList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(roomList, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getRoomById/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        Optional<Room> roomObj = roomRepository.findById(id);
        if (roomObj.isPresent()) {
            return new ResponseEntity<>(roomObj.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addRoom")
    public ResponseEntity<Room> addRoom(@RequestBody Room room) {
        try {
            Room roomObj = roomRepository.save(room);
            return new ResponseEntity<>(roomObj, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
