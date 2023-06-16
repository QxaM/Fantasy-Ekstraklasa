package com.kodilla.fantasy.service;

import com.kodilla.fantasy.domain.Mail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SimpleEmailServiceTests {

    @InjectMocks
    private SimpleEmailService emailService;
    @Mock
    private JavaMailSender javaMailSender;

    @Test
    void shouldSend() {
        //Given
        Mail mail = Mail.builder()
                .mailTo("user@test.com")
                .message("Test")
                .subject("Test Subject")
                .build();

        SimpleMailMessage mimeMessage = new SimpleMailMessage();
        mimeMessage.setTo("user@test.com");
        mimeMessage.setSubject("Test Subject");
        mimeMessage.setText("Test");

        //When
        emailService.send(mail);

        //Then
        verify(javaMailSender, times(1)).send(mimeMessage);
    }
}
