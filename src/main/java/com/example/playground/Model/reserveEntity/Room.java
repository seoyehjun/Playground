package com.example.playground.Model.reserveEntity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Room
{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roomId;

    private int price;
    //private int roomNumber;
    private String type; // A 혹은 B
    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations;

}
