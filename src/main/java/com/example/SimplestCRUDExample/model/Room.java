package com.example.SimplestCRUDExample.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="Rooms")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private int beds;

    @Column
    private Long price;

    @Column
    private Date availableFrom;

    @Column
    private Date availableTo;


}
