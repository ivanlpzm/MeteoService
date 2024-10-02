package com.smoke.meteoservice.domain.port.out.kafka;

import java.util.concurrent.CompletableFuture;

public interface KafkaProducerService {

    void sendMessage(Object message);
}
