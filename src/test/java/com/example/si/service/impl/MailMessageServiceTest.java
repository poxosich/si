package com.example.si.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;


@ExtendWith(SpringExtension.class)
class MailMessageServiceTest {

    @InjectMocks
    private MailMessageService mailMessageService;

    @Mock
    private JavaMailSender javaMailSender;

    @Test
    void testSent() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String message = "Test Message";

        mailMessageService.sent(to, subject, message);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender).send(captor.capture());

        SimpleMailMessage sent = captor.getValue();
        assertEquals(to, sent.getTo()[0]);
        assertEquals(subject, sent.getSubject());
        assertEquals(message, sent.getText());
    }
}