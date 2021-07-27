package eu.taegener.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "person", schema = "demo", uniqueConstraints = @UniqueConstraint(columnNames = {"firstname", "surname"}))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Person {

    @Id
    @PositiveOrZero
    @Column(name = "p_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long p_id;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "city", nullable = false)
    private String city;
}
