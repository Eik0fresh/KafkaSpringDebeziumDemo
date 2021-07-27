package eu.taegener.demo.democonsumer.config.kafka;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import eu.taegener.demo.democonsumer.properties.KafkaConsumerProperties;

import eu.taegener.demo.democonsumer.receiver.KafkaApplicationConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;


@EnableKafka
@Configuration
public class KafkaConsumerConfig {
  @Autowired private KafkaConsumerProperties kafkaProperties;

  private List<String> supportedEventTypes = Arrays.asList("person_created");

  private static final Logger log = Logger.getLogger( KafkaConsumerConfig.class.getName() );

  public ConsumerFactory<String, byte[]> consumerFactory() {
      Map<String, Object> configProps = new HashMap<>();
      configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapAddress());
      configProps.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getGroupId());
      return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), new ByteArrayDeserializer());
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, byte[]> kafkaListenerContainerFactory() {
      ConcurrentKafkaListenerContainerFactory<String, byte[]> factory = new ConcurrentKafkaListenerContainerFactory<String, byte[]>();
      log.info("kafkaListenerContainerFactory");
      factory.setConsumerFactory(consumerFactory());
      factory.setRecordFilterStrategy(record -> {
          for (Header header : record.headers()) {
              if (header.key().equals("eventType") && supportedEventTypes.contains(new String(header.value()))) {
                  return false;
              }
          }
          return true;
      });
      return factory;
  }
}
