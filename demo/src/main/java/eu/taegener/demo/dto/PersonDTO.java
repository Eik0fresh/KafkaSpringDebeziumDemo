package eu.taegener.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (Json Object which is send or received)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonDTO {
    private Long p_id;
    private String firstname;
    private String surname;
    private String city;
}
