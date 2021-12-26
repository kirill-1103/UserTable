package ru.krey.table.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.krey.table.model.Participant;
import ru.krey.table.repos.ParticipantRepo;
import ru.krey.table.service.UserService;

import java.util.List;

@Controller
@RequestMapping(value = "/users")
public class ParticipantsController {

    private final ParticipantRepo participantRepo;

    @Autowired
    UserService userService;

    public ParticipantsController(ParticipantRepo participantRepo) {
        this.participantRepo = participantRepo;
    }

    @GetMapping
    public String allUsers(Model model){
        Participant currentUser = participantRepo.findByLogin(getCurrentUserLogin());
        if(currentUser==null || !currentUser.isStatus()){
            SecurityContextHolder.clearContext();
            return "redirect:/login";
        }
        List<Participant> all = participantRepo.findAll();
        model.addAttribute("all",all);
        return "users";
    }

    @CrossOrigin
    @PostMapping("/block")
    public String block(@RequestBody String[] logins){
        if(!checkCurrentUser()){
            return "redirect://";
        }
        for(String l : logins){
            userService.blockParticipant(l);
        }
        return "redirect://";
    }

    @CrossOrigin
    @PostMapping("/unblock")
    public String unblock(@RequestBody String[] logins){
        if(!checkCurrentUser()){
            return "redirect://";
        }
        for(String l:logins){
            userService.unblockParticipant(l);
        }
        return "redirect://";
    }

    @CrossOrigin
    @PostMapping("/remove")
    public String remove(@RequestBody String[] logins){
        if(!checkCurrentUser()){
            return "redirect://";
        }
        for(String l:logins){
            userService.removeParticipant(l);
        }
        return "redirect://";
    }

    public String getCurrentUserLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public boolean checkCurrentUser(){
        Participant u = participantRepo.findByLogin(getCurrentUserLogin());
        return u != null && u.isStatus();
    }
}
