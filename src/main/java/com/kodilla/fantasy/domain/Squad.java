package com.kodilla.fantasy.domain;

import com.kodilla.fantasy.domain.exception.NotEnoughFundsException;
import com.kodilla.fantasy.domain.exception.SquadAlreadyFullException;
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
@Table(name = "SQUADS")
public class Squad {
    private final static BigDecimal MAX_VALUE = BigDecimal.valueOf(30000000);
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
    @Column(name = "SQUAD_VALUE")
    private BigDecimal currentValue;

    public Squad(String name, List<Player> players) {
        this.name = name;
        this.players = players;
    }

    public void addPlayer(Player player) throws SquadAlreadyFullException, NotEnoughFundsException {
        if(getPlayers().size() >= 11) {
            throw new SquadAlreadyFullException();
        }

        BigDecimal valueLeft = MAX_VALUE.subtract(currentValue);
        if(player.getValue().compareTo(valueLeft) > 0) {
            throw new NotEnoughFundsException();
        }

        players.add(player);
        currentValue = currentValue.add(player.getValue());
    }

    public void removePlayer(Player player) {
        if(players.contains(player)){
            players.remove(player);
            currentValue = currentValue.subtract(player.getValue());
        }
    }
}
