package com.example.stats_service.service;

import com.example.stats_service.event.DnaVerifiedEvent;
import com.example.stats_service.repository.DnaStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Processes DNA verification events and updates statistics.
 * This service is called by the event subscriber when events are received.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StatsEventProcessor {
    private final DnaStatsRepository dnaStatsRepository;

    /**
     * Processes a DNA verification event by updating the appropriate statistics counter.
     * Evicts the stats cache to ensure fresh data is returned.
     *
     * @param event the DNA verification event containing the verification result
     */
    @Transactional
    @CacheEvict(value = "stats", key = "'global'")
    public void processEvent(DnaVerifiedEvent event) {
        if (event.isMutant()) {
            int updatedRows = dnaStatsRepository.incrementMutantCount();
            log.info("Incremented mutant count from event - rows affected: {}", updatedRows);
        } else {
            int updatedRows = dnaStatsRepository.incrementHumanCount();
            log.info("Incremented human count from event - rows affected: {}", updatedRows);
        }
    }
}
