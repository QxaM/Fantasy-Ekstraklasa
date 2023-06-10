package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.Squad;
import com.kodilla.fantasy.domain.User;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDbService {

    private final UserRepository repository;

    public List<User> getUsers() {
        return repository.findAll();
    }

    public User getUser(Long id) throws ElementNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("User with given id: " + id + " does not exist"));
    }

    public User saveUser(User user) {
        return repository.save(user);
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    public User createSquad(Long id, String squadName) throws ElementNotFoundException {
        User foundUser = getUser(id);
        Squad newSquad = new Squad(squadName);
        foundUser.setSquad(newSquad);
        return repository.save(foundUser);
    }
}
