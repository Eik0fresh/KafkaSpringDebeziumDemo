package eu.taegener.demo.repository;

import eu.taegener.demo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

  
  @Query(value="SELECT firstname FROM Person p WHERE p.lastname LIKE", nativeQuery=true)
  Person getPersonBySurname(String surname);
  
}
