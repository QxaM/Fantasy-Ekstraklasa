package com.kodilla.fantasy.repository;

import com.kodilla.fantasy.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveUser() {
        //Given
        User user = new User("User 1");

        //When
        userRepository.save(user);
        Long id = user.getId();

        //Then
        Optional<User> foundUser = userRepository.findById(id);
        assertTrue(foundUser.isPresent());
        assertEquals("User 1", foundUser.get().getUsername());

        //CleanUp
        userRepository.deleteById(id);
    }

    @Test
    void shouldGetUsers() {
        //Given
        User user1 = new User("User 1");
        User user2 = new User("User 2");
        userRepository.saveAll(List.of(user1, user2));
        Long id1 = user1.getId();
        Long id2 = user2.getId();

        //When
        List<User> users = userRepository.findAll();

        //Then
        assertAll(() -> assertEquals(2, users.size()),
                () -> assertEquals("User 1", users.get(0).getUsername()),
                () -> assertEquals("User 2", users.get(1).getUsername()));

        //CleanUp
        userRepository.deleteById(id1);
        userRepository.deleteById(id2);
    }

    @Test
    void shouldGetUser() {
        //Given
        User user = new User("User 1");
        userRepository.save(user);
        Long id = user.getId();

        //When
        Optional<User> foundUser = userRepository.findById(id);

        //Then
        assertTrue(foundUser.isPresent());
        assertEquals("User 1", foundUser.get().getUsername());

        //CleanUp
        userRepository.deleteById(id);
    }

    @Test
    void shouldDeleteUser() {
        //Given
        User user = new User("User 1");
        userRepository.save(user);
        Long id = user.getId();

        //When
        userRepository.deleteById(id);

        //Then
        Optional<User> foundUser = userRepository.findById(id);
        assertFalse(foundUser.isPresent());
    }
}
