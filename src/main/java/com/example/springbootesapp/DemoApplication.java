package com.example.springbootesapp;

import java.util.Collection;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;


@SpringBootApplication
public class DemoApplication {

    private Random random = new Random();
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(KafkaTemplate<String, String> kafkaTemplate) {
        return args -> {
            while (true) {
                Thread.sleep(5000);
		kafkaTemplate.send("<TOPIC-NAME>", String.valueOf(random.nextInt(100)), String.valueOf(random.nextInt(100)));
            }
        };
	}
	
    @KafkaListener(topics = "<TOPIC-NAME>")
    public void processMessage(ConsumerRecord<String, String> record) {
		LOGGER.info("Message from topic = Key {}, Value {}", record.key(), record.value());
	}
}
