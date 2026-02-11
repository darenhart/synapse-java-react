package com.example.dna_demo.repository;

import com.example.dna_demo.entity.DnaRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DnaRecordRepository extends JpaRepository<DnaRecord, Long> {

    /**
     * Checks if a DNA record exists by its hash
     * @param dnaHash SHA-256 hash of the DNA sequence
     * @return true if exists, false otherwise
     */
    boolean existsByDnaHash(String dnaHash);

    /**
     * Finds a DNA record by its hash
     * @param dnaHash SHA-256 hash of the DNA sequence
     * @return Optional containing the record if found
     */
    Optional<DnaRecord> findByDnaHash(String dnaHash);
}
