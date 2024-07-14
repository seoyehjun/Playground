package com.example.playground.Model.dto;

import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Data
public class EventDto
{
    public EventDto(String title, LocalDate startDate, LocalDate endDate, String color)
    {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.color = color;
    }
    private String title; // 이벤트 제목
    private LocalDate startDate; // 시작일
    private LocalDate endDate; // 종료일
    private String color; // 이벤트 색상

}
