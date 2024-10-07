package com.smoke.meteoservice.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smoke.meteoservice.domain.model.kafka.KafkaTemperatureMessage;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class KafkaProducerImplTest {

    private static final String TOPIC = "my-Topic";
    private static final double LATITUDE = 40.0;
    private static final double LONGITUDE = -3.0;
    private static final double TEMPERATURE = 15.0;
    private static final String JSON_MESSAGE = "{\"latitude\":40.0,\"longitude\":-3.0,\"temperature\":15.0}";

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private KafkaProducerImpl kafkaProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(kafkaProducer, "kafkaEnabled", true);
    }

    @Test
    @DisplayName("Should send message successfully to Kafka")
    void sendMessage_shouldSendSuccessfully() throws JsonProcessingException {
        KafkaTemperatureMessage message = new KafkaTemperatureMessage(LATITUDE, LONGITUDE, TEMPERATURE);

        when(objectMapper.writeValueAsString(any(KafkaTemperatureMessage.class))).thenReturn(JSON_MESSAGE);

        ListenableFuture<SendResult<String, String>> future = mock(ListenableFuture.class);
        when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(future);

        kafkaProducer.sendMessage(message);

        verify(kafkaTemplate, times(1)).send(any(ProducerRecord.class));
    }

    @Test
    @DisplayName("Should handle JsonProcessingException without sending message to Kafka")
    void sendMessage_shouldHandleJsonProcessingException() throws JsonProcessingException {
        KafkaTemperatureMessage message = new KafkaTemperatureMessage(LATITUDE, LONGITUDE, TEMPERATURE);

        when(objectMapper.writeValueAsString(any(KafkaTemperatureMessage.class))).thenThrow(JsonProcessingException.class);

        kafkaProducer.sendMessage(message);

        verify(kafkaTemplate, never()).send(any(ProducerRecord.class));
    }

    @Test
    @DisplayName("Should execute onSuccess callback when Kafka message is successfully sent")
    void sendMessage_shouldExecuteOnSuccessCallback() throws JsonProcessingException {
        KafkaTemperatureMessage message = new KafkaTemperatureMessage(LATITUDE, LONGITUDE, TEMPERATURE);

        when(objectMapper.writeValueAsString(any(KafkaTemperatureMessage.class))).thenReturn(JSON_MESSAGE);

        ListenableFuture<SendResult<String, String>> future = mock(ListenableFuture.class);
        when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(future);

        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC, JSON_MESSAGE);
        SendResult<String, String> sendResult = new SendResult<>(producerRecord, null);

        doAnswer(invocation -> {
            ListenableFutureCallback<SendResult<String, String>> callback = invocation.getArgument(0);
            callback.onSuccess(sendResult);
            return null;
        }).when(future).addCallback(any(ListenableFutureCallback.class));

        kafkaProducer.sendMessage(message);

        verify(kafkaTemplate, times(1)).send(any(ProducerRecord.class));
        verify(future, times(1)).addCallback(any(ListenableFutureCallback.class));
    }

    @Test
    @DisplayName("Should execute onFailure callback when Kafka message sending fails")
    void sendMessage_shouldExecuteOnFailureCallback() throws JsonProcessingException {
        KafkaTemperatureMessage message = new KafkaTemperatureMessage(LATITUDE, LONGITUDE, TEMPERATURE);

        when(objectMapper.writeValueAsString(any(KafkaTemperatureMessage.class))).thenReturn(JSON_MESSAGE);

        ListenableFuture<SendResult<String, String>> future = mock(ListenableFuture.class);
        when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(future);

        doAnswer(invocation -> {
            ListenableFutureCallback<SendResult<String, String>> callback = invocation.getArgument(0);
            callback.onFailure(new RuntimeException("Error sending message to Kafka"));
            return null;
        }).when(future).addCallback(any(ListenableFutureCallback.class));

        kafkaProducer.sendMessage(message);

        verify(kafkaTemplate, times(1)).send(any(ProducerRecord.class));
        verify(future, times(1)).addCallback(any(ListenableFutureCallback.class));
    }
}
