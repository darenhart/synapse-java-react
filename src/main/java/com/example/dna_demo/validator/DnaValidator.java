package com.example.dna_demo.validator;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class DnaValidator {

    private static final Pattern VALID_DNA_PATTERN = Pattern.compile("^[ATCG]+$");
    private static final int MIN_SIZE = 4;

    /**
     * Validates a DNA sequence array
     * @param dna Array of strings representing DNA sequence
     * @throws IllegalArgumentException if validation fails
     */
    public void validate(String[] dna) {
        validateNotNull(dna);
        validateNotEmpty(dna);
        validateSquareMatrix(dna);
        validateMinimumSize(dna);
        validateRows(dna);
    }

    private void validateNotNull(String[] dna) {
        if (dna == null) {
            throw new IllegalArgumentException("DNA sequence cannot be null");
        }
    }

    private void validateNotEmpty(String[] dna) {
        if (dna.length == 0) {
            throw new IllegalArgumentException("DNA sequence cannot be empty");
        }
    }

    private void validateMinimumSize(String[] dna) {
        if (dna.length < MIN_SIZE) {
            throw new IllegalArgumentException("DNA matrix must be at least 4x4 to detect sequences");
        }
    }

    private void validateSquareMatrix(String[] dna) {
        int n = dna.length;
        for (String row : dna) {
            if (row == null) {
                throw new IllegalArgumentException("DNA row cannot be null");
            }
            if (row.length() != n) {
                throw new IllegalArgumentException("DNA matrix must be NxN (square)");
            }
        }
    }

    private void validateRows(String[] dna) {
        for (int i = 0; i < dna.length; i++) {
            String row = dna[i];
            if (!VALID_DNA_PATTERN.matcher(row).matches()) {
                throw new IllegalArgumentException(
                    String.format("Invalid character found in row %d. Only A, T, C, G are allowed", i)
                );
            }
        }
    }
}
