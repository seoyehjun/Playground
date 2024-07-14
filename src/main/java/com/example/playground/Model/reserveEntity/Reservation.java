package com.example.playground.Model.reserveEntity;

import com.example.playground.Model.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
public class Reservation
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "roomId")
    private Room room;

    private Date startDay;
    private Date endDay;

    // 다대일 관계 설정
    @ManyToOne
    @JoinColumn(name = "id") // 외래 키 이름
    private Member member;



    // Other fields, getters, setters...
}
