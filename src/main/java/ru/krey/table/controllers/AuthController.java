package ru.krey.table.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.krey.table.model.Participant;
import ru.krey.table.service.UserService;

import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String newUser(){
        return "new";
    }

    @PostMapping("/signup")
    public String addUser(@Valid Participant participant, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            Map<String, String> errorsMap = getErrors(bindingResult);
            model.addAttribute("Errors",errorsMap);
            model.addAttribute("participant",participant);
            return "new";
        }else {
            if (!userService.addParticipant(participant)) {
                model.addAttribute("participant_exists", true);
                return "new";
            }
            return "redirect:/login";
        }
    }

    private Map<String, String> getErrors(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage
        );
        return bindingResult.getFieldErrors().stream().collect(collector);
    }

}

