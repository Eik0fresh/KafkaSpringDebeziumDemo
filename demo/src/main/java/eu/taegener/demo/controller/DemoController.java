package eu.taegener.demo.controller;

import eu.taegener.demo.command.IPersonCommandService;
import eu.taegener.demo.dto.PersonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@RequestMapping("/person")
@Controller
public class DemoController {

    @Autowired
    IPersonCommandService personCommandService;

    /**
     * Method to create a person in the database
     * URI: http://localhost:8080/person/create
     * @param personDTO Json object without the p_id
     * @return p_id and HttpStatus CREATED or HttpStatus UNPROCESSABLE_ENTITY
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> handleCreate(@RequestBody PersonDTO personDTO) {
        Optional<Long> id = personCommandService.handleCreatePerson(personDTO);
        if (id.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<Long>(id.get(), HttpStatus.CREATED);
    }

    /**
     * Method to delete a person in the database
     * URI: http://localhost:8080/person/delete
     * @param person Json object without the p_id
     * @return p_id and HttpStatus OK or HttpStatus UNPROCESSABLE_ENTITY
     */
    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> handleDelete(@RequestBody PersonDTO person) {
        Optional<Long> id = personCommandService.handleDeletePerson(person);
        if (id.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<Long>(id.get(), HttpStatus.OK);
    }
}
