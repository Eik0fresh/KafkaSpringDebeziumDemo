package eu.taegener.demo.democonsumer.receiver;

import java.io.IOException;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaApplicationConsumer {
  @Autowired ObjectMapper mapper;

  private static final Logger log = Logger.getLogger( KafkaApplicationConsumer.class.getName() );

  /**
   * KafkaListener: listens to the topic person and does sth. if the eventType is person_created
   * invoked by a Message (through the DebeziumTransformer
   * @param message
   * @throws IOException
   */
  @KafkaListener(topics = {"person"}, containerFactory = "kafkaListenerContainerFactory")
  public void kafkaListener(@Payload byte[] message) throws IOException {
    log.info("KafkaListener triggered");
    JsonNode json = mapper.readTree(message);
    JsonNode payload = json.has("schema") ? json.get("payload") : json;

    if (!payload.has("eventType") || !payload.has("eventData")) {
      return;
    }

    switch (payload.get("eventType").asText()) {
      case "person_created":
        log.info("Hallo hier wurde eine Person angelegt");
        break;
      default:
        break;
    }
  }
}
