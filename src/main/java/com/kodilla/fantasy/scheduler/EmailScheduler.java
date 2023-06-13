package com.kodilla.fantasy.scheduler;

import com.kodilla.fantasy.domain.Mail;
import com.kodilla.fantasy.domain.User;
import com.kodilla.fantasy.service.SimpleEmailService;
import com.kodilla.fantasy.service.UserDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailScheduler {

    private final static String SUBJECT = "Next round of Fantasy Extraklasa!";

    private final UserDbService userDbService;
    private final SimpleEmailService emailService;

    @Scheduled(cron = "0 0 21 ? * MON")
    public void sendEmailsToUsers() {
        List<User> users = userDbService.getUsers();
        for(User user: users) {
            emailService.send(
                    Mail.builder()
                            .mailTo(user.getEmail())
                            .subject(SUBJECT)
                            .message(buildMessage(user))
                            .build()
            );
        }
    }

    private String buildMessage(User user) {
        return "Hey! You have " + user.getPoints() + " after last round!";
    }
}
