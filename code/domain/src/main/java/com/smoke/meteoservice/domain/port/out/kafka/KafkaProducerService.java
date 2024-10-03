package com.smoke.meteoservice.domain.port.out.kafka;

public interface KafkaProducerService {

    void sendMessage(Object message);
}
