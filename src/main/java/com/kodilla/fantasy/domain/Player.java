package com.kodilla.fantasy.domain;

import com.kodilla.fantasy.decorator.PlayerValues;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
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
    private List<Squad> squads;
    @Column(name = "POINTS")
    private int points = 0;

    public Player(Long id, Long apiFootballId, String firstname, String lastname, int age, BigDecimal value, Position position, Team team) {
        this.id = id;
        this.apiFootballId = apiFootballId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.value = value;
        this.position = position;
        this.team = team;
    }

    public Player(Long apiFootballId, String firstname, String lastname, int age, BigDecimal value, Position position) {
        this.apiFootballId = apiFootballId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.value = value;
        this.position = position;
    }

    public Player(Long apiFootballId, String name, String firstname, String lastname, int age, BigDecimal value, Position position, Team team) {
        this.apiFootballId = apiFootballId;
        this.name = name;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.value = value;
        this.position = position;
        this.team = team;
    }

    public Player(Long apiFootballId, String firstname, String lastname, int age, BigDecimal value, Position position, Team team, List<Squad> squads) {
        this.apiFootballId = apiFootballId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.value = value;
        this.position = position;
        this.team = team;
        this.squads = squads;
    }

    public void addPoints(PlayerValues decorator) {
        this.points = decorator.getPoints(this.points);
    }
}
