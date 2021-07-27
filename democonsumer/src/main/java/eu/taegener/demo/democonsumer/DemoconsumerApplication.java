package eu.taegener.demo.democonsumer;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.taegener.demo.democonsumer.receiver.KafkaApplicationConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.logging.Logger;

@SpringBootApplication
public class DemoconsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoconsumerApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

}
