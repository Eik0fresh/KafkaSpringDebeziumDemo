package eu.taegener.demo.democonsumer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("kafka")
@Configuration
@Getter
@Setter
public class KafkaConsumerProperties {
  private String bootstrapAddress;
  private String groupId;
}
