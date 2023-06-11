package com.kodilla.fantasy.livescore.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventDataDto extends EventDto {
    @JsonProperty("events")
    private List<EventDto> events;

    public EventDataDto(String event, String playerName, int team, List<EventDto> events) {
        super(event, playerName, team);
        this.events = events;
    }
}
