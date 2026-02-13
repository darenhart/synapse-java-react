package com.example.stats_service.event;

import com.example.stats_service.service.StatsEventProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DnaEventSubscriberTest {

    @Mock
    private StatsEventProcessor statsEventProcessor;

    @Mock
    private Message message;

    private DnaEventSubscriber subscriber;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        subscriber = new DnaEventSubscriber(statsEventProcessor);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testOnMessageMutantEvent() throws Exception {
        // Given
        DnaVerifiedEvent event = new DnaVerifiedEvent("hash123", true);
        String json = objectMapper.writeValueAsString(event);
        when(message.getBody()).thenReturn(json.getBytes());

        // When
        subscriber.onMessage(message, null);

        // Then
        verify(statsEventProcessor).processEvent(any(DnaVerifiedEvent.class));
    }

    @Test
    void testOnMessageHumanEvent() throws Exception {
        // Given
        DnaVerifiedEvent event = new DnaVerifiedEvent("hash456", false);
        String json = objectMapper.writeValueAsString(event);
        when(message.getBody()).thenReturn(json.getBytes());

        // When
        subscriber.onMessage(message, null);

        // Then
        verify(statsEventProcessor).processEvent(any(DnaVerifiedEvent.class));
    }

    @Test
    void testOnMessageWithInvalidData() {
        // Given
        byte[] invalidData = "invalid data".getBytes();
        when(message.getBody()).thenReturn(invalidData);

        // When
        subscriber.onMessage(message, null);

        // Then
        verify(statsEventProcessor, never()).processEvent(any());
    }

    @Test
    void testOnMessageMultipleTimes() throws Exception {
        // Given
        DnaVerifiedEvent event1 = new DnaVerifiedEvent("hash1", true);
        DnaVerifiedEvent event2 = new DnaVerifiedEvent("hash2", false);
        String json1 = objectMapper.writeValueAsString(event1);
        String json2 = objectMapper.writeValueAsString(event2);

        when(message.getBody())
            .thenReturn(json1.getBytes())
            .thenReturn(json2.getBytes());

        // When
        subscriber.onMessage(message, null);
        subscriber.onMessage(message, null);

        // Then
        verify(statsEventProcessor, times(2)).processEvent(any(DnaVerifiedEvent.class));
    }
}
