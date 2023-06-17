package com.kodilla.fantasy.domain;

import com.sun.istack.NotNull;
import lombok.*;

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
    @Setter
    private Long id;
    @Column(name = "LEAGUE_NAME")
    private String name;
    @ManyToMany(mappedBy = "leagues")
    private List<User> users = new ArrayList<>();

    @Builder
    public League(String name, List<User> users) {
        this.name = name;
        this.users = users;
    }
}
