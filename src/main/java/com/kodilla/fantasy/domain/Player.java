package com.kodilla.fantasy.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

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
    @Column(name = "FIRSTNAME")
    private String firstname;
    @Column(name = "LASTNAME")
    private String lastname;
    @Column(name = "AGE")
    private int age;
    @Column(name = "VALUE")
    private BigDecimal value;
    @Column(name = "POSITION")
    private Position position;

    public Player(Long apiFootballId, String firstname, String lastname, int age, BigDecimal value, Position position) {
        this.apiFootballId = apiFootballId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.value = value;
        this.position = position;
    }
}
