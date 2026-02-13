package com.example.stats_service.service;

import com.example.stats_service.event.DnaVerifiedEvent;
import com.example.stats_service.repository.DnaStatsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatsEventProcessorTest {

    @Mock
    private DnaStatsRepository dnaStatsRepository;

    private StatsEventProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new StatsEventProcessor(dnaStatsRepository);
    }

    @Test
    void testProcessMutantEvent() {
        // Given
        DnaVerifiedEvent event = new DnaVerifiedEvent("hash123", true);
        when(dnaStatsRepository.incrementMutantCount()).thenReturn(1);

        // When
        processor.processEvent(event);

        // Then
        verify(dnaStatsRepository).incrementMutantCount();
        verify(dnaStatsRepository, never()).incrementHumanCount();
    }

    @Test
    void testProcessHumanEvent() {
        // Given
        DnaVerifiedEvent event = new DnaVerifiedEvent("hash456", false);
        when(dnaStatsRepository.incrementHumanCount()).thenReturn(1);

        // When
        processor.processEvent(event);

        // Then
        verify(dnaStatsRepository).incrementHumanCount();
        verify(dnaStatsRepository, never()).incrementMutantCount();
    }

    @Test
    void testProcessMultipleMutantEvents() {
        // Given
        DnaVerifiedEvent event1 = new DnaVerifiedEvent("hash1", true);
        DnaVerifiedEvent event2 = new DnaVerifiedEvent("hash2", true);
        when(dnaStatsRepository.incrementMutantCount()).thenReturn(1);

        // When
        processor.processEvent(event1);
        processor.processEvent(event2);

        // Then
        verify(dnaStatsRepository, times(2)).incrementMutantCount();
        verify(dnaStatsRepository, never()).incrementHumanCount();
    }

    @Test
    void testProcessMultipleHumanEvents() {
        // Given
        DnaVerifiedEvent event1 = new DnaVerifiedEvent("hash3", false);
        DnaVerifiedEvent event2 = new DnaVerifiedEvent("hash4", false);
        when(dnaStatsRepository.incrementHumanCount()).thenReturn(1);

        // When
        processor.processEvent(event1);
        processor.processEvent(event2);

        // Then
        verify(dnaStatsRepository, times(2)).incrementHumanCount();
        verify(dnaStatsRepository, never()).incrementMutantCount();
    }

    @Test
    void testProcessMixedEvents() {
        // Given
        DnaVerifiedEvent mutantEvent = new DnaVerifiedEvent("mutant", true);
        DnaVerifiedEvent humanEvent = new DnaVerifiedEvent("human", false);
        when(dnaStatsRepository.incrementMutantCount()).thenReturn(1);
        when(dnaStatsRepository.incrementHumanCount()).thenReturn(1);

        // When
        processor.processEvent(mutantEvent);
        processor.processEvent(humanEvent);

        // Then
        verify(dnaStatsRepository).incrementMutantCount();
        verify(dnaStatsRepository).incrementHumanCount();
    }
}
