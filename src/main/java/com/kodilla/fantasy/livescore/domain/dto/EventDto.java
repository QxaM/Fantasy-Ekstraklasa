package com.kodilla.fantasy.livescore.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventDto {
    @JsonProperty("event")
    private String event;
    @JsonProperty("player_name")
    private String playerName;
    @JsonProperty("team")
    private int team;
}
