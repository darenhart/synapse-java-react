package com.example.dna_demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "dna_records", indexes = {
    @Index(name = "idx_dna_hash", columnList = "dna_hash", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DnaRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dna_hash", nullable = false, unique = true, length = 64)
    private String dnaHash;

    @Column(name = "dna_sequence", nullable = false, columnDefinition = "TEXT")
    private String dnaSequence;

    @Column(name = "is_mutant", nullable = false)
    private Boolean isMutant;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public DnaRecord(String dnaHash, String dnaSequence, Boolean isMutant) {
        this.dnaHash = dnaHash;
        this.dnaSequence = dnaSequence;
        this.isMutant = isMutant;
    }
}
