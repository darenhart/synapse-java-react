package com.example.stats_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Event published when a DNA sequence has been verified.
 * Contains the DNA hash and verification result (mutant or human).
 *
 * NOTE: This is an intentional duplicate of dna-demo's event class.
 * Each service owns its domain model for complete independence.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DnaVerifiedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String dnaHash;
    private boolean isMutant;
    private long timestamp;

    /**
     * Constructor without timestamp (auto-generated).
     *
     * @param dnaHash SHA-256 hash of the DNA sequence
     * @param isMutant true if mutant, false if human
     */
    public DnaVerifiedEvent(String dnaHash, boolean isMutant) {
        this.dnaHash = dnaHash;
        this.isMutant = isMutant;
        this.timestamp = System.currentTimeMillis();
    }
}
