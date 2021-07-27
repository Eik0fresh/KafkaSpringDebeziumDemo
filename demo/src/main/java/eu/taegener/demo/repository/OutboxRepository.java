package eu.taegener.demo.repository;

import eu.taegener.demo.model.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {

}
