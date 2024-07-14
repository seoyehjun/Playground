package com.example.playground.Model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class DayDto
{
    long dayId;
    Date availabledate;
}
