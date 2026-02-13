package com.example.dna_demo.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DnaEventPublisherTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    private DnaEventPublisher publisher;

    @BeforeEach
    void setUp() {
        publisher = new DnaEventPublisher(redisTemplate);
    }

    @Test
    void testPublishSuccess() {
        // Given
        DnaVerifiedEvent event = new DnaVerifiedEvent("hash123", true);

        // When
        publisher.publish(event);

        // Then
        ArgumentCaptor<String> channelCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<DnaVerifiedEvent> eventCaptor = ArgumentCaptor.forClass(DnaVerifiedEvent.class);

        verify(redisTemplate).convertAndSend(channelCaptor.capture(), eventCaptor.capture());

        assertEquals("dna-events", channelCaptor.getValue());
        assertEquals("hash123", eventCaptor.getValue().getDnaHash());
        assertTrue(eventCaptor.getValue().isMutant());
    }

    @Test
    void testPublishMutantEvent() {
        // Given
        DnaVerifiedEvent event = new DnaVerifiedEvent("mutantHash", true);

        // When
        publisher.publish(event);

        // Then
        verify(redisTemplate).convertAndSend(eq("dna-events"), any(DnaVerifiedEvent.class));
    }

    @Test
    void testPublishHumanEvent() {
        // Given
        DnaVerifiedEvent event = new DnaVerifiedEvent("humanHash", false);

        // When
        publisher.publish(event);

        // Then
        verify(redisTemplate).convertAndSend(eq("dna-events"), any(DnaVerifiedEvent.class));
    }

    @Test
    void testPublishFailureThrowsException() {
        // Given
        DnaVerifiedEvent event = new DnaVerifiedEvent("hash123", true);
        doThrow(new RuntimeException("Redis connection failed"))
            .when(redisTemplate).convertAndSend(anyString(), any());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            publisher.publish(event);
        });

        assertEquals("Event publishing failed", exception.getMessage());
    }

    @Test
    void testPublishMultipleEvents() {
        // Given
        DnaVerifiedEvent event1 = new DnaVerifiedEvent("hash1", true);
        DnaVerifiedEvent event2 = new DnaVerifiedEvent("hash2", false);

        // When
        publisher.publish(event1);
        publisher.publish(event2);

        // Then
        verify(redisTemplate, times(2)).convertAndSend(eq("dna-events"), any(DnaVerifiedEvent.class));
    }
}
