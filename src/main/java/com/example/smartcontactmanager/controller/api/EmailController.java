package com.example.smartcontactmanager.controller.api;

import com.example.smartcontactmanager.helper.CustomResponse;
import com.example.smartcontactmanager.helper.EmailRequest;
import com.example.smartcontactmanager.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/email")
public class EmailController {

    private EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    // Send Email

    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest emailRequest){
        emailService.sendEmailWithHtml(emailRequest.getTo(),emailRequest.getSubject(),emailRequest.getMessage());
        return ResponseEntity.ok(
                CustomResponse.builder().message("Email Send Successfully !!")
                        .httpStatus(HttpStatus.OK).success(true).build()
        );
    }

    // Send Email With File

    @PostMapping("/send-with-file")
    public ResponseEntity<CustomResponse> sendWithFile(@RequestPart EmailRequest emailRequest, @RequestPart MultipartFile file) throws IOException {
        emailService.sendEmailWithFile(emailRequest.getTo(),emailRequest.getSubject(),emailRequest.getMessage(),file.getInputStream());
        return ResponseEntity.ok(
                CustomResponse.builder().message("Email Send Successfully !!")
                        .httpStatus(HttpStatus.OK).success(true).build()
        );
    }

}
