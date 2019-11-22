package net.atos.spring_webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class AutoMailingService {
    JavaMailSender javaMailSender;
    @Autowired
    public AutoMailingService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    public void sendEmail(String to, String subject, String content){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        javaMailSender.send(simpleMailMessage);
    }
}
