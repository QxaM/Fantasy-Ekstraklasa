package com.kodilla.fantasy.apifootball.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerResponseDto {
    @JsonProperty("player")
    private ApiFootballPlayerDto player;
    @JsonProperty("statistics")
    private StatisticsDto statistics;
}
