package ru.krey.table.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.krey.table.model.Participant;

import java.util.List;

@Controller
public class DefaultController {

    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping("/")
    public String getDefault(){
        final List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        for(Object principal : allPrincipals){
            if(principal instanceof Participant){
                Participant participant = (Participant) principal;
            }
        }
        return "redirect:/users";
    }
}
