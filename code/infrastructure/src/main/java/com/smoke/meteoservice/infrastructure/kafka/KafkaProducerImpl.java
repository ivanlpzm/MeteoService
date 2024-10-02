package com.smoke.meteoservice.infrastructure.kafka;

import com.smoke.meteoservice.domain.port.out.kafka.KafkaProducerService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerImpl implements KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerImpl.class);
    private static final String TOPIC = "my-Topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage(Object message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(createRecord(message)).completable();
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Message successfully sent: {}", result.getProducerRecord().value());
            } else {
                logger.error("Error sending the message: {}", ex.getMessage(), ex);
            }
        });

    }

    private ProducerRecord<String, String> createRecord(Object data) {
        return new ProducerRecord<>(TOPIC, data.toString());
    }
}
