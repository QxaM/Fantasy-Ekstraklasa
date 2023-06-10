package com.kodilla.fantasy.controller;

import com.kodilla.fantasy.domain.Squad;
import com.kodilla.fantasy.domain.User;
import com.kodilla.fantasy.domain.dto.SquadDto;
import com.kodilla.fantasy.domain.dto.UserDto;
import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.mapper.SquadMapper;
import com.kodilla.fantasy.mapper.UserMapper;
import com.kodilla.fantasy.service.UserDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserDbService service;
    private final UserMapper mapper;
    private final SquadMapper squadMapper;

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) throws ElementNotFoundException {
        User foundUser = service.getUser(id);
        UserDto mappedUser = mapper.mapToUserDto(foundUser);
        return ResponseEntity.ok(mappedUser);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        User userToSave = mapper.mapToUser(userDto);
        User savedUser = service.saveUser(userToSave);
        UserDto mappedUser = mapper.mapToUserDto(savedUser);
        return ResponseEntity.ok(mappedUser);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) throws ElementNotFoundException {
        User foundUser = service.getUser(userDto.getId());
        User userToSave = new User(
                foundUser.getId(),
                userDto.getUsername(),
                foundUser.getLeagues(),
                foundUser.getSquad()
        );
        User savedUser = service.saveUser(userToSave);
        UserDto mappedUser = mapper.mapToUserDto(savedUser);
        return ResponseEntity.ok(mappedUser);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createSquad(@PathVariable Long id,
                                            @RequestBody SquadDto squadDto) throws ElementNotFoundException {
        Squad mappedSquad = squadMapper.mapToSquad(squadDto);
        service.createSquad(id, mappedSquad);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
