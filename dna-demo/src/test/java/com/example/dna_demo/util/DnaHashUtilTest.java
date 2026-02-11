package com.example.dna_demo.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DnaHashUtilTest {

    @Test
    void shouldGenerateConsistentHashForSameDna() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};

        String hash1 = DnaHashUtil.generateHash(dna);
        String hash2 = DnaHashUtil.generateHash(dna);

        assertEquals(hash1, hash2);
    }

    @Test
    void shouldGenerateDifferentHashForDifferentDna() {
        String[] dna1 = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        String[] dna2 = {"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"};

        String hash1 = DnaHashUtil.generateHash(dna1);
        String hash2 = DnaHashUtil.generateHash(dna2);

        assertNotEquals(hash1, hash2);
    }

    @Test
    void shouldGenerateHashWithCorrectLength() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};

        String hash = DnaHashUtil.generateHash(dna);

        // SHA-256 produces 64 hex characters
        assertEquals(64, hash.length());
    }

    @Test
    void shouldConvertDnaToString() {
        String[] dna = {"ATGC", "TGCA", "GCTA", "CATG"};

        String result = DnaHashUtil.dnaToString(dna);

        assertEquals("ATGC|TGCA|GCTA|CATG", result);
    }

    @Test
    void shouldConvertStringBackToDna() {
        String dnaString = "ATGC|TGCA|GCTA|CATG";

        String[] result = DnaHashUtil.stringToDna(dnaString);

        assertArrayEquals(new String[]{"ATGC", "TGCA", "GCTA", "CATG"}, result);
    }

    @Test
    void shouldRoundTripConversion() {
        String[] original = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};

        String stringForm = DnaHashUtil.dnaToString(original);
        String[] converted = DnaHashUtil.stringToDna(stringForm);

        assertArrayEquals(original, converted);
    }
}
