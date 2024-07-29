package com.caito.ecommerce.usersevice.services.impl;

import com.caito.ecommerce.usersevice.api.exceptions.customs.EmailSendingException;
import com.caito.ecommerce.usersevice.api.exceptions.customs.FileIOException;
import com.caito.ecommerce.usersevice.services.contracts.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    @Value("${application.email}")
    private String email;

    private final String ERROR_MESSAGE = "no se pudo enviar el e-mail";
    private final String ERROR_TEMPLATE = "no se pudo cargar el template";

    @Override
    public void sendEmail(String[] to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(email);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (MailException e) {
            log.error("--> ERROR: ".concat(ERROR_MESSAGE));
            throw new EmailSendingException(ERROR_MESSAGE);
        }
    }

    @Override
    public void sendEmailWithAttachment(String[] to, String subject, String body, File file) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true,
                    StandardCharsets.UTF_8.name());
            helper.setFrom(email);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);
            helper.addAttachment(file.getName(), file);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("--> ERROR: ".concat(ERROR_MESSAGE));
            throw new EmailSendingException(ERROR_MESSAGE);
        }

    }

    @Override
    public void sendEmailWithTemplate(String[] to, String subject, String templateName, Map<String, String> data) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true,
                    StandardCharsets.UTF_8.name());
            helper.setFrom(email);
            helper.setTo(to);
            helper.setSubject(subject);
            String template = this.getTemplate(templateName);
            for (Map.Entry<String, String> m : data.entrySet()) {
                template = template.replace("${" + m.getKey() + "}", m.getValue());
            }
            helper.setText(template, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("--> ERROR: ".concat(ERROR_MESSAGE));
            throw new EmailSendingException(ERROR_MESSAGE);
        }
    }

    private String getTemplate(String templateName) {
        templateName = "templates/".concat(templateName);
        ClassPathResource resource = new ClassPathResource(templateName);
        try {
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("--> ERROR: ".concat(ERROR_TEMPLATE));
            throw new FileIOException(ERROR_TEMPLATE);
        }
    }
}
