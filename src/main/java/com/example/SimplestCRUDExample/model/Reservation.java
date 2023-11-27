package com.example.SimplestCRUDExample.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="Reservations")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String resNumber;

    @Column
    private Long roomId;

    @Column
    private Date checkIn;

    @Column
    private Date checkOut;

    @Column
    private int guestCount;

    @Column
    private boolean cancelled;


}
