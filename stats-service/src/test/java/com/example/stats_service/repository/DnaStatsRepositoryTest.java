package com.example.stats_service.repository;

import com.example.stats_service.entity.DnaStats;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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
}
