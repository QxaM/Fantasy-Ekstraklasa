package com.kodilla.fantasy.domain;

import com.kodilla.fantasy.decorator.PlayerValues;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public static class PlayerBuilder {
        private Long apiFootballId;
        private String name;
        private String firstname;
        private String lastname;
        private int age;
        private BigDecimal value;
        private Position position;
        private Team team;

        public PlayerBuilder apiFootballId(Long apiFootballId) {
            this.apiFootballId = apiFootballId;
            return this;
        }
        public PlayerBuilder name(String name) {
            this.name = name;
            return this;
        }
        public PlayerBuilder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }
        public PlayerBuilder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }
        public PlayerBuilder age(int age) {
            this.age = age;
            return this;
        }
        public PlayerBuilder value(BigDecimal value) {
            this.value = value;
            return this;
        }
        public PlayerBuilder position(Position position) {
            this.position = position;
            return this;
        }
        public PlayerBuilder team(Team team) {
            this.team = team;
            return this;
        }

        public Player build() {
            return new Player(apiFootballId, name, firstname, lastname, age, value, position, team);
        }
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
}
