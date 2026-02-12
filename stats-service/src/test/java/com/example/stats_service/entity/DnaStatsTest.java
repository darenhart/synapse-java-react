package com.example.stats_service.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DnaStatsTest {

    @Test
    void getRatio_shouldReturnCorrectRatio() {
        // Given - 40 mutants out of 140 total = 0.2857
        DnaStats stats = new DnaStats();
        stats.setCountMutantDna(40L);
        stats.setCountHumanDna(100L);

        // When
        double ratio = stats.getRatio();

        // Then
        assertEquals(0.2857, ratio, 0.001);
    }

    @Test
    void getRatio_shouldReturn0WhenNoData() {
        // Given
        DnaStats stats = new DnaStats();
        stats.setCountMutantDna(0L);
        stats.setCountHumanDna(0L);

        // When
        double ratio = stats.getRatio();

        // Then
        assertEquals(0.0, ratio);
    }

    @Test
    void getRatio_shouldReturn1WhenAllMutants() {
        // Given
        DnaStats stats = new DnaStats();
        stats.setCountMutantDna(100L);
        stats.setCountHumanDna(0L);

        // When
        double ratio = stats.getRatio();

        // Then
        assertEquals(1.0, ratio);
    }

    @Test
    void getRatio_shouldReturnCorrectDecimal() {
        // Given - 1 mutant out of 3 total = 0.333...
        DnaStats stats = new DnaStats();
        stats.setCountMutantDna(1L);
        stats.setCountHumanDna(2L);

        // When
        double ratio = stats.getRatio();

        // Then
        assertEquals(0.333, ratio, 0.001);
    }

    @Test
    void getRatio_shouldHandleLargeNumbers() {
        // Given
        DnaStats stats = new DnaStats();
        stats.setCountMutantDna(1000000L);
        stats.setCountHumanDna(2000000L);

        // When
        double ratio = stats.getRatio();

        // Then
        assertEquals(0.333, ratio, 0.001);
    }
}
