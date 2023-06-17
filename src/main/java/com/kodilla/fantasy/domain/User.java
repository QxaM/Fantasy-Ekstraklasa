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
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "ID")
    @Setter
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

    @Builder
    public User(String username, String email, List<League> leagues, Squad squad, int points) {
        this.username = username;
        this.email = email;
        this.leagues = leagues;
        this.squad = squad;
        this.points = points;
    }
}
