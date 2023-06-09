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
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Column(name = "USERNAME")
    private String username;
    @ManyToMany(mappedBy = "users")
    private List<League> leagues;

    public User(String username, List<League> leagues) {
        this.username = username;
        this.leagues = leagues;
    }
}
