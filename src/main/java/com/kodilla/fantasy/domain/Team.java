package com.kodilla.fantasy.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TEAMS")
public class Team {
    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Column(name = "API_FOOTBALL_ID")
    private Long apiFootballId;
    @Column(name = "NAME")
    private String name;
    @Column(name = "CODE")
    private String code;

    public Team(Long apiFootballId, String name, String code) {
        this.apiFootballId = apiFootballId;
        this.name = name;
        this.code = code;
    }
}
