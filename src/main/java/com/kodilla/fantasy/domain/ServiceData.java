package com.kodilla.fantasy.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "SERVICE_DATA")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceData {
    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Column(name = "ROUND")
    private Integer round;

    public ServiceData(Integer round) {
        this.round = round;
    }
}
