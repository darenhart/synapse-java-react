package com.example.stats_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "dna_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DnaStats {

    @Id
    @Column(name = "id")
    private Integer id = 1;

    @Column(name = "count_mutant_dna", nullable = false)
    private Long countMutantDna = 0L;

    @Column(name = "count_human_dna", nullable = false)
    private Long countHumanDna = 0L;

    @UpdateTimestamp
    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;

    public double getRatio() {
        long total = countMutantDna + countHumanDna;
        if (total == 0) {
            return 0.0;
        }
        return (double) countMutantDna / total;
    }
}
