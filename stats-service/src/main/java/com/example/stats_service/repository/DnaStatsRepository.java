package com.example.stats_service.repository;

import com.example.stats_service.entity.DnaStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DnaStatsRepository extends JpaRepository<DnaStats, Integer> {

    default DnaStats getStats() {
        return findById(1).orElseGet(() -> {
            DnaStats stats = new DnaStats();
            stats.setId(1);
            stats.setCountMutantDna(0L);
            stats.setCountHumanDna(0L);
            return save(stats);
        });
    }
}
