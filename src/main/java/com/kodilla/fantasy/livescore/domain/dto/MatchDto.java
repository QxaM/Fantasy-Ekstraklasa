package com.kodilla.fantasy.livescore.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchDto {
    @JsonProperty("match_id")
    private String matchId;
    @JsonProperty("team_1")
    private LiveScoreTeamDto team1;
    @JsonProperty("team_2")
    private LiveScoreTeamDto team2;
}
