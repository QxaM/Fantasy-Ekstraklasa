package com.kodilla.fantasy.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LEAGUES")
public class League {
    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Column(name = "LEAGUE_NAME")
    private String name;
    @ManyToMany(mappedBy = "leagues")
    private List<User> users = new ArrayList<>();

    public League(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public League(String name) {
        this.name = name;
    }

    public League(String name, List<User> users) {
        this.name = name;
        this.users = users;
    }
}
