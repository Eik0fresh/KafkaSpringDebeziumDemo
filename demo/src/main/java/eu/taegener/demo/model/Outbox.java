package eu.taegener.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "outbox", schema = "demo")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Outbox {
    @Id
    @Column(name = "event_id", nullable = false)
    private UUID uuid;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "root_aggregate_type", nullable = false)
    private String rootAggregateType;

    @Column(name = "root_aggregate_id", nullable = false)
    private String rootAggregateId;

    @Column(name = "aggregate_type", nullable = false)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false)
    private String aggregateId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "payload", nullable = false)
    private String payload;
}
