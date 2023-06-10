package com.kodilla.fantasy.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SquadDto {
    private Long id;
    private String name;
    private BigDecimal currentValue;
    private List<PlayerDto> players;
}
