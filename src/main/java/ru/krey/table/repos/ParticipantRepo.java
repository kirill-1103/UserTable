package ru.krey.table.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.krey.table.model.Participant;

public interface ParticipantRepo extends JpaRepository<Participant,Long> {
    Participant findByLogin(String login);
}
