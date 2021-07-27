package eu.taegener.demo.command;

import eu.taegener.demo.dto.PersonDTO;

import java.util.Optional;

public interface IPersonCommandService {
    Optional<Long> handleCreatePerson(PersonDTO person);

    Optional<Long> handleDeletePerson(PersonDTO person);
}
