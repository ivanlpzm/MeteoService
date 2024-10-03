package com.smoke.meteoservice.infrastructure.kafka;

import com.smoke.meteoservice.domain.port.out.kafka.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public void sendMessage(Object message) {
        log.debug("Sending message: {}", message.toString());
        
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(createRecord(message));
        
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
    }

    private ProducerRecord<String, String> createRecord(Object data) {
        return new ProducerRecord<>(TOPIC, data.toString());
    }
}
