package eu.taegener.demo.repository;

import eu.taegener.demo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PersonRepository extends JpaRepository<Person, Long> {

  
  @Query(value="SELECT firstname FROM Person p WHERE p.lastname LIKE", nativeQuery=true)
  Person getPersonBySurname(String surname);
  
}
