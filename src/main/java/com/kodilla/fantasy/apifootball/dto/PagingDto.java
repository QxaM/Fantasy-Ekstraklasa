package com.kodilla.fantasy.apifootball.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PagingDto {
    private int current;
    private int total;
}
