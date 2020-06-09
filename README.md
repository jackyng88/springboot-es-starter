# springboot-es-starter

# Spring Boot Event Streams Starter Application
Sample producer and consumer

## Introduction
1. This is a simple Spring Boot application that will produce messages every five seconds to a topic as well as constantly listen and consume from said topic.
2. To use this application you will need to update a few values in the code with your Event Streams instance values.


## Pre-requisites
1. An IBM Event Streams instance installed on IBM Cloud Pak for Integration.
2. Spring Boot
3. Maven v3.6.x+
4. Spring Boot CLI v2.x.x+


## Getting the necessary Event Streams details (Bootstrap Server, API Key, Certificate)
1. This markdown assumes you have an Event Streams instance installed on IBM Cloud Pak for Integration. As such the following steps
that are outlined reflect that process. These steps may differ from say IBM Event Streams on IBM Cloud for example.
2. Login to the Cloud Pak for Integration Platform Navigator and choose your installed Event Streams instance.
3. From the menu on your left select Topics. 
4. Note - If you have not already created a Topic yet, you can do so now here as a Topic will be necessary.
5. If you already have a Topic or you have just created one, then in the top-right corner click on the `Connect to this Cluster` link.
6. Copy down the `Bootstrap Server Address`
7. Click the `Generate API Key` button and copy down your API Key. 
  - Note: You should give your API Key produce and consume at the very least. You can give it the ability to produce, consume, create topics and schemas if you so choose.
8. Lastly download your `Java truststore` file under the Certificates section. This file by default is named `es-cert.jks`


__Summary__ 

These are the following items obtained in the previous section.
- Topic name
- Bootstrap server address
- API Key
- Java truststore certificate (es-cert.jks)


## Replacing necessary field names
1. Now that you have your Event Streams credentials traverse to the `src/main/resources/application.properties` file. 

```properties
# Spring server config
# server.port=8080
# spring.application.name=spring-boot-es-app


# Event Streams connection configuration
spring.kafka.jaas.enabled=true
spring.kafka.jaas.login-module=org.apache.kafka.common.security.plain.PlainLoginModule
spring.kafka.bootstrap-servers=<es-bootstrap-address>

# Event Streams security options.
# Note that the Spring Kafka expects ssl.truststore-location to be in src/webapp/...
spring.kafka.jaas.options.username=token
spring.kafka.jaas.options.password=<ES-APIKey>
spring.kafka.properties.security.protocol=SASL_SSL
spring.kafka.properties.sasl.mechanism=PLAIN
spring.kafka.ssl.protocol=TLSv1.2
spring.kafka.ssl.truststore-location=<path-to-es-cert-jks-file>
spring.kafka.ssl.truststore-password=password

# Kafka Producer
spring.kafka.producer.client-id=event-streams-kafka
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer

# Kafka Consumer
spring.kafka.consumer.group-id=<unique-consumer-group-id>
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
```

2. Replace `<es-bootstrap-address>` with your own Bootstrap server Address 
```properties 
spring.kafka.bootstrap-servers=<es-bootstrap-address>
```

3. Replace `<ES-APIKey>` with your API key.
```properties
spring.kafka.jaas.options.password=<ES-APIKey>
```

4. (Important) For this next step you will need to create the `src/main/webapp` folder as well as copy your Event Streams Java truststore certificate into the `src/main/webapp` folder.
Spring Kafka expects the truststore to be in that folder. For example if you have your file inside an ssl folder inside webapp it would be
```properties
spring.kafka.ssl.truststore-location=/ssl/es-cert.jks
```

5. Replace `<unique-consumer-group-id>` with a value of your choosing. This is to prevent conflicts in case another consumer group has the same id.
```properties
spring.kafka.consumer.group-id=<unique-consumer-group-id>
  ```
  
6. We are now done with the `application.properties` file and now we need to open the `src/main/java/com/example/springbootesapp/DemoApplication.java` file. 

7. On line 35, replace `<TOPIC-NAME>` with your own created Topic's name.
```java
kafkaTemplate.send("<TOPIC-NAME>", String.valueOf(random.nextInt(100)), String.valueOf(random.nextInt(100)));
```

8. Do the same thing on line 40 as well; replace with your created Topic's name.
```java
@KafkaListener(topics = "<TOPIC-NAME>")
```

9. We have now replaced all the necessary fields.


## Running the application
1. Go to the root of the project folder. Run the application
```shell
mvn spring-boot:run
```

2. Your consumer should return some log messages similar to the below.
```log
2020-06-09 10:36:23.647  INFO 41146 --- [ntainer#0-0-C-1] c.e.springbootesapp.DemoApplication      : Message from topic = Key 1, Value 55
2020-06-09 10:36:28.650  INFO 41146 --- [ntainer#0-0-C-1] c.e.springbootesapp.DemoApplication      : Message from topic = Key 70, Value 35
```
