package com.kodilla.fantasy.repository;

import com.kodilla.fantasy.domain.League;
import com.kodilla.fantasy.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Override
    List<User> findAll();

    List<User> findUserByLeaguesId(Long id);
}
