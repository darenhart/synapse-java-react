package com.example.dna_demo.service;

import com.example.dna_demo.entity.DnaRecord;
import com.example.dna_demo.event.DnaEventPublisher;
import com.example.dna_demo.event.DnaVerifiedEvent;
import com.example.dna_demo.repository.DnaRecordRepository;
import com.example.dna_demo.util.DnaHashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DnaService {

    private final MutantDetector mutantDetector;
    private final DnaRecordRepository dnaRecordRepository;
    private final DnaEventPublisher dnaEventPublisher;

    /**
     * Verifies if DNA is mutant and saves the result to database
     * This method is idempotent - calling it multiple times with same DNA
     * will not create duplicate records or increment stats multiple times
     *
     * @param dna Array of DNA strings
     * @return true if mutant, false if human
     */
    @Transactional
    public boolean verifyAndSave(String[] dna) {
        // Generate hash for duplicate detection
        String dnaHash = DnaHashUtil.generateHash(dna);

        // Check if DNA already verified (idempotent operation)
        if (dnaRecordRepository.existsByDnaHash(dnaHash)) {
            log.debug("DNA already verified, returning cached result for hash: {}", dnaHash);
            DnaRecord existingRecord = dnaRecordRepository.findByDnaHash(dnaHash)
                .orElseThrow(() -> new IllegalStateException("Record should exist"));
            return existingRecord.getIsMutant();
        }

        // Verify if DNA is mutant
        boolean isMutant = mutantDetector.isMutant(dna);
        log.info("DNA verification result - Mutant: {}, Hash: {}", isMutant, dnaHash);

        // Save DNA record
        String dnaSequence = DnaHashUtil.dnaToString(dna);
        DnaRecord record = new DnaRecord(dnaHash, dnaSequence, isMutant);
        dnaRecordRepository.save(record);

        // Publish DNA verification event to Redis Pub/Sub
        DnaVerifiedEvent event = new DnaVerifiedEvent(dnaHash, isMutant);
        dnaEventPublisher.publish(event);
        log.info("Published DNA verification event - Mutant: {}, Hash: {}", isMutant, dnaHash);

        return isMutant;
    }

    /**
     * Gets the DNA verification result from database (if exists)
     * @param dna Array of DNA strings
     * @return Optional with result if DNA was previously verified
     */
    public Boolean getCachedResult(String[] dna) {
        String dnaHash = DnaHashUtil.generateHash(dna);
        return dnaRecordRepository.findByDnaHash(dnaHash)
            .map(DnaRecord::getIsMutant)
            .orElse(null);
    }
}
