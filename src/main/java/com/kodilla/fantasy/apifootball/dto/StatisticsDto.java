package com.kodilla.fantasy.apifootball.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticsDto {
    @JsonProperty("team")
    private ApiFootballTeamDto team;
    @JsonProperty("games")
    private GamesDto games;
}
