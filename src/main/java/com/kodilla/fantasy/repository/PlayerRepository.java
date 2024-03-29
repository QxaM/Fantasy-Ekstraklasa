package com.kodilla.fantasy.repository;

import com.kodilla.fantasy.domain.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends PagingAndSortingRepository<Player, Long> {

    @Override
    Page<Player> findAll(Pageable pageable);

    List<Player> findPlayersByTeamId(Long teamId);
}
