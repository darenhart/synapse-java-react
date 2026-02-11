package com.example.dna_demo.config;

import com.example.dna_demo.entity.DnaStats;
import com.example.dna_demo.repository.DnaStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {

    private final DnaStatsRepository dnaStatsRepository;

    @Override
    public void run(String... args) {
        // Initialize stats table with a single row if it doesn't exist
        if (dnaStatsRepository.findById(1).isEmpty()) {
            DnaStats stats = new DnaStats();
            stats.setId(1);
            stats.setCountMutantDna(0L);
            stats.setCountHumanDna(0L);
            dnaStatsRepository.save(stats);
            log.info("Initialized DNA stats table with default values");
        } else {
            log.info("DNA stats table already initialized");
        }
    }
}
