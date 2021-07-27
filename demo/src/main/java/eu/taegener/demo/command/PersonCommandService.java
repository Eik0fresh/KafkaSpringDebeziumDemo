package eu.taegener.demo.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.taegener.demo.dto.PersonDTO;
import eu.taegener.demo.model.Outbox;
import eu.taegener.demo.model.Person;
import eu.taegener.demo.repository.OutboxRepository;
import eu.taegener.demo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Command class to manipulate the db objects or to send a message
 */
@Service
@Transactional
public class PersonCommandService implements IPersonCommandService {

    @Autowired
    PersonRepository personRepository;
    @Autowired
    OutboxRepository outboxRepository;
    @Autowired
    ObjectMapper mapper;

    @Override
    @Transactional
    public Optional<Long> handleCreatePerson(PersonDTO person) {
        if (person.getFirstname() == null || person.getSurname() == null) {
            return Optional.empty();
        }

        Person newPerson = new Person(null, person.getFirstname(), person.getSurname(), person.getCity());
        // Repository provides functionalities to get and save db objects
        personRepository.save(newPerson);

        // Save sth in the outboxRepository to invoke a KafkaMessage (monitored by Debezium)
        JsonNode jsonNode = mapper.convertValue(newPerson, JsonNode.class);
        Outbox o = new Outbox(UUID.randomUUID(), 1, "person", newPerson.getP_id().toString(), "person", newPerson.getP_id().toString(), "person_created", jsonNode.toString());
        outboxRepository.save(o);
        outboxRepository.delete(o);

        return Optional.of(newPerson.getP_id());
    }

    @Override
    public Optional<Long> handleDeletePerson(PersonDTO person) {
        Person deletePerson = personRepository.getOne(person.getP_id());
        personRepository.delete(deletePerson);
        return Optional.of(person.getP_id());
    }
}
