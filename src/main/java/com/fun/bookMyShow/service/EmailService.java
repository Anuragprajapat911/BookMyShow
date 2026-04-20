package com.fun.bookMyShow.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String name) {
        try{

            System.out.println("LOGIN MAIL TRIGGERED");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(to);
            helper.setSubject("welcome to Ghost show");

            String contant=
                    "<h2>Welcome " + name + " 🎬</h2>" +
                            "<p>Your Welcome Ghost show.</p>" +
                            "<p>Start booking your favorite movies now.</p>";
            helper.setText(contant,true);
            mailSender.send(mimeMessage);


        }catch (Exception e){
            System.out.println("Email sending failed"+e.getMessage());
        }
    }
}
