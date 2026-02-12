package com.example.dna_demo.repository;

import com.example.dna_demo.entity.DnaStats;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DnaStatsRepositoryTest {

    @Autowired
    private DnaStatsRepository repository;

    @Test
    void getStats_shouldReturnStatsOrDefault() {
        // When
        DnaStats stats = repository.getStats();

        // Then
        assertNotNull(stats);
        assertNotNull(stats.getCountMutantDna());
        assertNotNull(stats.getCountHumanDna());
    }

    @Test
    void incrementMutantCount_shouldExecute() {
        // When
        int result = repository.incrementMutantCount();

        // Then
        assertTrue(result >= 0);
    }

    @Test
    void incrementHumanCount_shouldExecute() {
        // When
        int result = repository.incrementHumanCount();

        // Then
        assertTrue(result >= 0);
    }
}
