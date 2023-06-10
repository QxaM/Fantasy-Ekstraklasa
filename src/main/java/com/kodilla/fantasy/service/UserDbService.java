package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.Squad;
import com.kodilla.fantasy.domain.User;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDbService {

    private final UserRepository repository;
    private final SquadDbService squadDbService;

    public List<User> getUsers() {
        return repository.findAll();
    }

    public User getUser(Long id) throws ElementNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("User with given id: " + id + " does not exist"));
    }

    public Optional<User> getUserContainingSquad(Long id) {
        return repository.findUserBySquadId(id);
    }

    public User saveUser(User user) {
        return repository.save(user);
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public User createSquad(Long id, String squadName) throws ElementNotFoundException {
        User foundUser = getUser(id);
        Squad newSquad = new Squad(squadName);
        Squad oldSquad = foundUser.getSquad();
        foundUser.setSquad(newSquad);
        squadDbService.deleteSquad(oldSquad.getId());
        return repository.save(foundUser);
    }
}
