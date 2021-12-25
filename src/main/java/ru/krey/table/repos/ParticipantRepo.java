package ru.krey.table.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.krey.table.model.Participant;

public interface ParticipantRepo extends JpaRepository<Participant,Long> {
    Participant findByLogin(String login);

//    @Modifying
//    void deleteByLogin(String login);
}
