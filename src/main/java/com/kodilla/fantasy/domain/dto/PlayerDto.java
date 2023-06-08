package com.kodilla.fantasy.domain.dto;

import com.kodilla.fantasy.domain.Position;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDto {
    private Long id;
    private String firstname;
    private String lastname;
    private int age;
    private BigDecimal value;
    private Position position;
    private TeamDto team;
}
