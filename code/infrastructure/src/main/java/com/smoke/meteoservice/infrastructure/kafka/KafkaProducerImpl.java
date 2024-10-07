package com.smoke.meteoservice.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smoke.meteoservice.domain.port.out.kafka.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@Slf4j
public class KafkaProducerImpl implements KafkaProducerService {

    private static final String TOPIC = "my-Topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.kafka.enabled:true}") 
    private boolean kafkaEnabled;

    @Override
    public void sendMessage(Object message) {

        if (!kafkaEnabled) {
            log.warn("Kafka is disabled, skipping message send.");
            return;
        }

        try {
            String jsonMessage = convertToJson(message);
            log.debug("Sending message: {}", jsonMessage);

            ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(createRecord(jsonMessage));

            future.addCallback(new ListenableFutureCallback<>() {
                @Override
                public void onSuccess(SendResult<String, String> result) {
                    log.info("Message successfully sent: {}", result.getProducerRecord().value());
                }

                @Override
                public void onFailure(@NotNull Throwable ex) {
                    log.error("Error sending the message: {}", ex.getMessage(), ex);
                }
            });
        } catch (JsonProcessingException e) {
            log.error("Failed to convert message to JSON", e);
        }
    }

    private ProducerRecord<String, String> createRecord(String data) {
        return new ProducerRecord<>(TOPIC, data);
    }

    private String convertToJson(Object data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }
}
