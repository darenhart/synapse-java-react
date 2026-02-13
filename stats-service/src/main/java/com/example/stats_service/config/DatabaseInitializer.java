package com.example.stats_service.config;

import com.example.stats_service.entity.DnaStats;
import com.example.stats_service.repository.DnaStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initializes the DNA stats table on application startup.
 * Ensures that a stats record exists with initial values.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {
    private final DnaStatsRepository dnaStatsRepository;

    @Override
    public void run(String... args) {
        if (dnaStatsRepository.findById(1).isEmpty()) {
            DnaStats stats = new DnaStats();
            stats.setId(1);
            stats.setCountMutantDna(0L);
            stats.setCountHumanDna(0L);
            dnaStatsRepository.save(stats);
            log.info("Initialized DNA stats table with default values");
        } else {
            DnaStats existingStats = dnaStatsRepository.findById(1).get();
            log.info("DNA stats table already initialized - Mutants: {}, Humans: {}",
                existingStats.getCountMutantDna(), existingStats.getCountHumanDna());
        }
    }
}
