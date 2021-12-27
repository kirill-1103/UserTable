package ru.krey.table.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.krey.table.model.Participant;
import ru.krey.table.model.Role;
import ru.krey.table.repos.ParticipantRepo;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private  ParticipantRepo repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Participant participant = repo.findByLogin(login);
        if(participant==null){
            throw new UsernameNotFoundException(login);
        }
        repo.save(participant);
        return participant;
    }

    public boolean addParticipant(Participant participant){
        Participant participantFromDb = repo.findByLogin(participant.getLogin());

        if(participantFromDb != null){
            return false;
        }
        participant.setStatus(true);
        participant.setDate_reg(getCurrentDate());
        participant.setDate_last(getCurrentDate());
        participant.setRoles(Collections.singleton(Role.USER));
        participant.setPassword(passwordEncoder.encode(participant.getPassword()));
        repo.save(participant);
        return true;
    }

    public boolean blockParticipant(String login){
        return changeStatus(login,false);
    }

    public boolean unblockParticipant(String login){
        return changeStatus(login,true);
    }

    public void removeParticipant(String login){
        repo.delete(repo.findByLogin(login));
    }

    public boolean updateDateLast(String login){
        Participant p = repo.findByLogin(login);
        if(p==null){
            return false;
        }
        p.setDate_last(getCurrentDate());
        repo.save(p);
        return true;
    }

    public boolean changeRoleParticipant(String login){
        Participant p = repo.findByLogin(login);
        if(p==null){
            return false;
        }

        if (p.getRoles().contains(Role.USER)) {
            p.setRoles(Collections.singleton(Role.ADMIN));
        } else {
            p.setRoles(Collections.singleton(Role.USER));
        }
        repo.save(p);
        return true;
    }

    private boolean changeStatus(String login,boolean status){
        Participant participant = repo.findByLogin(login);
        if(participant == null){
            return false;
        }
        participant.setStatus(status);
        repo.save(participant);
        return true;
    }

    public String getCurrentDate(){
        return (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
    }
}
