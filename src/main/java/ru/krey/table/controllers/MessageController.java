package ru.krey.table.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.krey.table.model.Message;
import ru.krey.table.model.Participant;
import ru.krey.table.repos.ParticipantRepo;
import ru.krey.table.service.MessageService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping(value ="/message")
public class MessageController {

//    @Autowired
//    ParticipantRepo participantRepo;

    @Autowired
    MessageService service;

    @Autowired
    ParticipantRepo participantRepo;

    @CrossOrigin
    @PostMapping("/send")
    public String sendMessage(@RequestBody String[] body){
        String from = getCurrentUserLogin();
        String message  = body[body.length-1];
        if(message.length()>255){
            message = message.substring(0,254);
        }
        if(!(from == null || from.length()==0 || message.length()==0 || body.length<2)){
            for(int i =0  ;i<body.length-1;i++){
                service.addMessage(new Message(message,from,participantRepo.findByLogin(body[i]),getCurrentTime()));
            }
        }
        return "redirect:/users";
    }

    public boolean checkCurrentUser(){
        Participant u = participantRepo.findByLogin(getCurrentUserLogin());
        return u != null && u.isStatus();
    }

    public String getCurrentUserLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public String getCurrentTime(){
        Date date = new Date();
        String time = (new SimpleDateFormat("dd.MM.yyyy - hh:mm:ss")).format(date);
        return time;
    }
}
