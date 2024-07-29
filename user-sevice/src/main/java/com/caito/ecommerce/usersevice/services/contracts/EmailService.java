package com.caito.ecommerce.usersevice.services.contracts;

import java.io.File;
import java.util.Map;

public interface EmailService {
    void sendEmail(String[] to, String subject, String body);
    void sendEmailWithAttachment(String[] to, String subject, String body, File file);
    void sendEmailWithTemplate(String[] to, String subject, String templateName, Map<String, String> data);
}
