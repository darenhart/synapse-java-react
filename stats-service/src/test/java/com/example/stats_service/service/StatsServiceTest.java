package com.example.stats_service.service;

import com.example.stats_service.dto.StatsResponse;
import com.example.stats_service.entity.DnaStats;
import com.example.stats_service.repository.DnaStatsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private DnaStatsRepository statsRepository;

    @InjectMocks
    private StatsService statsService;

    @Test
    void getStats_shouldReturnCorrectStats() {
        // Given - 40 mutants out of 140 total = 0.2857
        DnaStats dnaStats = new DnaStats();
        dnaStats.setCountMutantDna(40L);
        dnaStats.setCountHumanDna(100L);
        when(statsRepository.getStats()).thenReturn(dnaStats);

        // When
        StatsResponse result = statsService.getStats();

        // Then
        assertEquals(40L, result.countMutantDna());
        assertEquals(100L, result.countHumanDna());
        assertEquals(0.2857, result.ratio(), 0.001);
        verify(statsRepository, times(1)).getStats();
    }

    @Test
    void getStats_shouldReturnZeroWhenNoData() {
        // Given
        DnaStats dnaStats = new DnaStats();
        dnaStats.setCountMutantDna(0L);
        dnaStats.setCountHumanDna(0L);
        when(statsRepository.getStats()).thenReturn(dnaStats);

        // When
        StatsResponse result = statsService.getStats();

        // Then
        assertEquals(0L, result.countMutantDna());
        assertEquals(0L, result.countHumanDna());
        assertEquals(0.0, result.ratio());
    }

    @Test
    void getStats_shouldCalculateCorrectRatio() {
        // Given - 3 mutants, 7 humans
        DnaStats dnaStats = new DnaStats();
        dnaStats.setCountMutantDna(3L);
        dnaStats.setCountHumanDna(7L);
        when(statsRepository.getStats()).thenReturn(dnaStats);

        // When
        StatsResponse result = statsService.getStats();

        // Then
        assertEquals(0.3, result.ratio(), 0.001);
    }

    @Test
    void getStats_shouldReturnRatio1WhenAllMutants() {
        // Given - all mutants
        DnaStats dnaStats = new DnaStats();
        dnaStats.setCountMutantDna(100L);
        dnaStats.setCountHumanDna(0L);
        when(statsRepository.getStats()).thenReturn(dnaStats);

        // When
        StatsResponse result = statsService.getStats();

        // Then
        assertEquals(1.0, result.ratio());
    }

    @Test
    void getStats_shouldCallRepositoryOnce() {
        // Given
        DnaStats dnaStats = new DnaStats();
        when(statsRepository.getStats()).thenReturn(dnaStats);

        // When
        statsService.getStats();

        // Then
        verify(statsRepository, times(1)).getStats();
    }
}
