package com.kodilla.fantasy.domain;

import com.kodilla.fantasy.decorator.PlayerValues;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PLAYERS")
public class Player {
    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "ID")
    @Setter
    private Long id;
    @Column(name = "API_FOOTBALL_ID")
    private Long apiFootballId;
    @Column(name = "NICKNAME")
    private String name;
    @Column(name = "FIRSTNAME")
    private String firstname;
    @Column(name = "LASTNAME")
    private String lastname;
    @Column(name = "AGE")
    private int age;
    @Column(name = "PLAYER_COST")
    private BigDecimal value;
    @Column(name = "POSITION")
    private Position position;
    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;
    @ManyToMany(mappedBy = "players")
    private List<Squad> squads = new ArrayList<>();
    @Column(name = "POINTS")
    private int points = 0;

    public void addPoints(PlayerValues decorator) {
        this.points = decorator.getPoints(this.points);
    }

    @Builder
    public Player(Long apiFootballId, String name, String firstname, String lastname, int age,
                  BigDecimal value, Position position, Team team, List<Squad> squads, int points) {
        this.apiFootballId = apiFootballId;
        this.name = name;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.value = value;
        this.position = position;
        this.team = team;
        this.squads = squads;
        this.points = points;
    }
}
