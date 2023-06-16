package com.kodilla.fantasy.scheduler;

import com.kodilla.fantasy.domain.Mail;
import com.kodilla.fantasy.domain.Squad;
import com.kodilla.fantasy.domain.User;
import com.kodilla.fantasy.service.SimpleEmailService;
import com.kodilla.fantasy.service.UserDbService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailSchedulerTests {

    private final static String SUBJECT = "Next round of Fantasy Extraklasa!";

    @InjectMocks
    private EmailScheduler emailScheduler;
    @Mock
    private UserDbService userDbService;
    @Mock
    private SimpleEmailService simpleEmailService;

    @Test
    void shouldEmailUsers() {
        //Given
        User user1 = new User(1L, "User 1", "user1@test.com", new ArrayList<>(), new Squad(), 1);
        User user2 = new User(2L, "User 2", "user2@test.com", new ArrayList<>(), new Squad(), 2);
        when(userDbService.getUsers()).thenReturn(List.of(user1, user2));

        //When
        emailScheduler.sendEmailsToUsers();

        //Then
        verify(simpleEmailService, times(2)).send(any(Mail.class));
    }
}
