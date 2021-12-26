package ru.krey.table.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.krey.table.model.Message;
import ru.krey.table.repos.MessageRepo;
import ru.krey.table.repos.ParticipantRepo;

import java.util.ArrayList;

@Service
public class MessageService {
    @Autowired
    MessageRepo repo;

    @Autowired
    ParticipantRepo participantRepo;

    public void addMessage(Message message){
        repo.save(message);
    }

    public ArrayList<Message> getMessagesToLogin(String login){
        return (ArrayList<Message>) repo.findAllByRecipient(participantRepo.findByLogin(login));
    }

}
