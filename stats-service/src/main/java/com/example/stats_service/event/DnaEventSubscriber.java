package com.example.stats_service.event;

import com.example.stats_service.service.StatsEventProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * Subscriber for DNA verification events from Redis Pub/Sub.
 * Listens to the "dna-events" channel and processes incoming events.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DnaEventSubscriber implements MessageListener {
    private final StatsEventProcessor statsEventProcessor;
    private final ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody(), StandardCharsets.UTF_8);
            // Extract the actual event data from the JSON structure
            // GenericJackson2JsonRedisSerializer wraps the object with @class field
            DnaVerifiedEvent event = objectMapper.readValue(json, DnaVerifiedEvent.class);

            log.info("Received DNA verification event - Hash: {}, Mutant: {}",
                event.getDnaHash(), event.isMutant());

            statsEventProcessor.processEvent(event);
        } catch (Exception e) {
            log.error("Failed to process DNA event", e);
            // TODO: Send to dead-letter queue or implement retry logic
        }
    }
}
