package ru.krey.table.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.krey.table.model.Message;
import ru.krey.table.model.Participant;
import ru.krey.table.model.Role;
import ru.krey.table.repos.ParticipantRepo;
import ru.krey.table.service.MessageService;
import ru.krey.table.service.UserService;

import javax.servlet.http.Part;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping(value = "/users")
public class ParticipantsController {

    private final ParticipantRepo participantRepo;

    @Autowired
    UserService userService;

    @Autowired
    private MessageService messageService;

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
        if(currentUser.getRoles().contains(Role.ADMIN)){
            return "redirect:/users/admin";
        }
        List<Participant> all = participantRepo.findAll();
        model.addAttribute("all",all);
        return "users";
    }

    @CrossOrigin
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
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

    @CrossOrigin
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin")
    public String getAdmin(Model model){
        Participant cp = participantRepo.findByLogin(getCurrentUserLogin());
        if(!cp.getRoles().contains(Role.ADMIN)){
            return "redirect:/users";
        }
        List<Participant> all = participantRepo.findAll();
        model.addAttribute("all",all);
        return "admin";
    }

    @CrossOrigin
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/changeRole")
    public String changeRole(@RequestBody String[] logins){
        if(!checkCurrentUser()){
            return "redirect://";
        }
        Participant cp = participantRepo.findByLogin(getCurrentUserLogin());
        if(!cp.getRoles().contains(Role.ADMIN)){
            return "redirect:/users";
        }
        for(String l:logins){
            userService.changeRoleParticipant(l);
        }
        return "redirect://";
    }

    @GetMapping("/account")
    public String getAccount(Model model){
        String login = getCurrentUserLogin();
        model.addAttribute("user",login);
        List<Message> messages = messageService.getMessagesToLogin(login);
        Collections.reverse(messages);
        model.addAttribute("messages", messages);
        return "account";
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
