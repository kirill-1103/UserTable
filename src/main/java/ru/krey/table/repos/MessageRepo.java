package ru.krey.table.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.krey.table.model.Message;
import ru.krey.table.model.Participant;

import java.util.ArrayList;
import java.util.List;

public interface MessageRepo extends JpaRepository<Message,Long> {
    List<Message> findAllByRecipient(Participant p);
}
