package com.kodilla.fantasy.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    @ManyToMany
    @JoinTable(
            name = "JOIN_USER_LEAGUE",
            joinColumns = {@JoinColumn(name = "LEAGUE_ID",
                                        referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_UD",
                                        referencedColumnName = "ID")}
    )
    private List<User> users;

    public League(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public League(String name, List<User> users) {
        this.name = name;
        this.users = users;
    }
}
