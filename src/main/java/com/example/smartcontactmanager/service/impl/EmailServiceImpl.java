package com.example.smartcontactmanager.service.impl;

import com.example.smartcontactmanager.helper.Message;
import com.example.smartcontactmanager.service.EmailService;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender javaMailSender;

    private Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        simpleMailMessage.setFrom("hardikkumar5991@gmail.com");
        javaMailSender.send(simpleMailMessage);
        logger.info("Email has been sent..");

    }

    @Override
    public void sendEmail(String[] to, String subject, String message) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        simpleMailMessage.setFrom("hardikkumar5991@gmail.com");
        javaMailSender.send(simpleMailMessage);

    }

    @Override
    public void sendEmailWithHtml(String to, String subject, String htmlContent) {

        MimeMessage simpleMailMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(simpleMailMessage,
                    true,"UTF-8");

            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom("hardikkumar5991@gmail.com");
            mimeMessageHelper.setText(htmlContent,true);
            javaMailSender.send(simpleMailMessage);
            logger.info("Email has been sent..");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEmailWithFile(String to, String subject, String message, File file) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setFrom("hardikkumar5991@gmail.com");
            helper.setTo(to);
            helper.setText(message);
            helper.setSubject(subject);
            FileSystemResource fileSystemResource = new FileSystemResource(file);
            helper.addAttachment(fileSystemResource.getFilename(),file);

            javaMailSender.send(mimeMessage);
            logger.info("Email send success !!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void sendEmailWithFile(String to, String subject, String message, InputStream is) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setFrom("hardikkumar5991@gmail.com");
            helper.setTo(to);
            helper.setText(message,true);
            helper.setSubject(subject);
            File file = new File("src/main/resources/email/test.png");
            Files.copy(is,file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            FileSystemResource fileSystemResource=new FileSystemResource(file);
            helper.addAttachment(fileSystemResource.getFilename(),file);

            javaMailSender.send(mimeMessage);
            logger.info("Email sent successfully");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Value("${mail.store.protocol}")
    String protocol;

    @Value("${mail.imaps.host}")
    String host;

    @Value("${mail.imaps.port}")
    String port;

    @Value("${spring.mail.username}")
    String username;

    @Value("${spring.mail.password}")
    String password;

    @Override
    public List<Message> getInboxMessages() {
        //code to receive email: get all email

        Properties configurations = new Properties();

        configurations.setProperty("mail.store.protocol","");
        configurations.setProperty("mail.imaps.host","");
        configurations.setProperty("mail.imaps.port","");

        Session session = Session.getDefaultInstance(configurations);
        try {
            Store store = session.getStore();

            store.connect(username, password);

            Folder inbox = store.getFolder("INBOX");

            inbox.open(Folder.READ_ONLY);

            jakarta.mail.Message[] messages = inbox.getMessages();

            List<Message> list = new ArrayList<>();

            for (jakarta.mail.Message message:messages){
                System.out.println(message.getSubject());

                String content = getContentFromEmailMessage(message);
                List<String> files = getFilesFromEmailMessage(message);
                System.out.println("____________________");

                list.add(Message.builder().subjects(message.getSubject()).content(content).files(files).build());
            }

            return list;

        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private List<String> getFilesFromEmailMessage(jakarta.mail.Message message) throws MessagingException, IOException {

        List<String> files = new ArrayList<>();
        if (message.isMimeType("multipart/*")) {

            Multipart content = (Multipart)message.getContent();

            for (int i = 0; i < content.getCount() ; i++) {
                BodyPart bodyPart = content.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())){

                    InputStream inputStream = bodyPart.getInputStream();
                    File file = new File("src/main/resources/email/"+bodyPart.getFileName());

                    // saved the file
                    Files.copy(inputStream,file.toPath(),StandardCopyOption.REPLACE_EXISTING);

                    // urls
                    files.add(file.getAbsolutePath());


                }
            }
        }

        return files;
    }

    private String getContentFromEmailMessage(jakarta.mail.Message message) throws MessagingException, IOException {

        if (message.isMimeType("text/plain") || message.isMimeType("text/html")){
            return (String)message.getContent();
        } else if (message.isMimeType("multipart/*")) {
          Multipart part = (Multipart)message.getContent();
          for (int i=0; i<part.getCount(); i++){
              BodyPart bodyPart = part.getBodyPart(i);
              if (bodyPart.isMimeType("text/plain")){
                  return (String) bodyPart.getContent();
              }
          }
        }

        return null;
    }
}
