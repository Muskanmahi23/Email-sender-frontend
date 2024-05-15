package com.example.smartcontactmanager.service;

import com.example.smartcontactmanager.helper.Message;
import  jakarta.mail.MessagingException;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface EmailService {

    // send email to single person
    void sendEmail(String to, String subject, String message);

    // send email to multiple person
    void sendEmail(String[] to, String subject, String message);

    // void sendEmailWithHtml
    void sendEmailWithHtml(String to, String subject, String htmlContent);

    // void send email with file
    void sendEmailWithFile(String to, String subject, String message, File file);

    void sendEmailWithFile(String to, String subject, String message, InputStream is);

    List<Message> getInboxMessages();

}
