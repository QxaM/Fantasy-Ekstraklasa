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
@Table(name = "SQUADS")
public class Squad {
    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "JOIN_PLAYERS_SQUADS",
            joinColumns = {@JoinColumn(name = "SQUAD_ID",
                                        referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "PLAYER_ID",
                                        referencedColumnName = "ID")}
    )
    private List<Player> players;

    public Squad(String name, List<Player> players) {
        this.name = name;
        this.players = players;
    }
}
