package com.example.dna_demo.repository;

import com.example.dna_demo.entity.DnaStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DnaStatsRepository extends JpaRepository<DnaStats, Integer> {

    /**
     * Increments the mutant DNA counter atomically
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE DnaStats s SET s.countMutantDna = s.countMutantDna + 1 WHERE s.id = 1")
    void incrementMutantCount();

    /**
     * Increments the human DNA counter atomically
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE DnaStats s SET s.countHumanDna = s.countHumanDna + 1 WHERE s.id = 1")
    void incrementHumanCount();

    /**
     * Gets the single stats record (creates if doesn't exist)
     * @return DnaStats record
     */
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
