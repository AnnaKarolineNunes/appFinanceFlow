package com.nunes.financeFlow.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class MockMailSender implements JavaMailSender {

    @Override
    public void send(SimpleMailMessage simpleMessage) {
        // Simulando o envio de e-mail
        System.out.println("E-mail simulado enviado para: " + simpleMessage.getTo()[0]);
        System.out.println("Assunto: " + simpleMessage.getSubject());
        System.out.println("Corpo: " + simpleMessage.getText());
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {

    }

    @Override
    public MimeMessage createMimeMessage() {
        return null;
    }

    @Override
    public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
        return null;
    }

    @Override
    public void send(MimeMessage... mimeMessages) throws MailException {

    }

    // Outros métodos podem ser adicionados para implementar a interface
}
