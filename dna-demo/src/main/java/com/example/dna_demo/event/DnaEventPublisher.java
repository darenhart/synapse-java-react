package com.example.dna_demo.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Publisher for DNA verification events.
 * Publishes events to Redis Pub/Sub channel for consumption by stats-service.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DnaEventPublisher {
    private static final String DNA_EVENTS_CHANNEL = "dna-events";

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Publishes a DNA verification event to Redis Pub/Sub.
     *
     * @param event the event to publish
     * @throws RuntimeException if event publishing fails
     */
    public void publish(DnaVerifiedEvent event) {
        try {
            redisTemplate.convertAndSend(DNA_EVENTS_CHANNEL, event);
            log.info("Published DNA verification event - Hash: {}, Mutant: {}",
                event.getDnaHash(), event.isMutant());
        } catch (Exception e) {
            log.error("Failed to publish DNA event: {}", event, e);
            throw new RuntimeException("Event publishing failed", e);
        }
    }
}
