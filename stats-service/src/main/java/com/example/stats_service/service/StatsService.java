package com.example.stats_service.service;

import com.example.stats_service.dto.StatsResponse;
import com.example.stats_service.entity.DnaStats;
import com.example.stats_service.repository.DnaStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsService {

    private final DnaStatsRepository statsRepository;

    /**
     * Gets DNA verification statistics
     * Results are cached in Redis for 1 minute
     * @return StatsResponse with counts and ratio
     */
    @Cacheable(value = "stats", key = "'global'")
    public StatsResponse getStats() {
        log.info("Fetching stats from database");
        DnaStats stats = statsRepository.getStats();

        return new StatsResponse(
            stats.getCountMutantDna(),
            stats.getCountHumanDna(),
            stats.getRatio()
        );
    }
}
