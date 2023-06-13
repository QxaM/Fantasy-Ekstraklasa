package com.kodilla.fantasy.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "EMAIL")
    private String email;
    @ManyToMany
    @JoinTable(
            name = "JOIN_LEAGUE_USER",
            joinColumns = {@JoinColumn(name = "USER_ID",
                    referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "LEAGUE_ID",
                    referencedColumnName = "ID")}
    )
    private List<League> leagues = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "SQUAD_ID")
    @Setter
    private Squad squad = new Squad();
    @Column(name = "POINTS")
    private int points = 0;

    @PostLoad
    private void loadPoints() {
        points = squad.getPlayers().stream()
                .map(Player::getPoints)
                .reduce(0, Integer::sum);
    }

    public User(String username) {
        this.username = username;
    }

    public User(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public User(String username, List<League> leagues, Squad squad) {
        this.username = username;
        this.leagues = leagues;
        this.squad = squad;
    }
}
