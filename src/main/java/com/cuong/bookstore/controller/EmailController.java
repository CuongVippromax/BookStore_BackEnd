package com.cuong.bookstore.controller;

import com.cuong.bookstore.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "EMAIL-CONTROLLER")
public class EmailController {
    private final MailService  mailService;
    @GetMapping("/send-email")
    public void send(@RequestParam String to , String subject, String content){
        log.info("Email sending to "+to);
        mailService.send(to, subject, content);
        log.info("Email sent to "+to);
    }
}
