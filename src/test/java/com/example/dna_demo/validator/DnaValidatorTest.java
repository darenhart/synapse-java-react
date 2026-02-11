package com.example.dna_demo.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DnaValidatorTest {

    private DnaValidator validator;

    @BeforeEach
    void setUp() {
        validator = new DnaValidator();
    }

    @Test
    void shouldAcceptValidDna() {
        String[] validDna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };

        assertDoesNotThrow(() -> validator.validate(validDna));
    }

    @Test
    void shouldThrowExceptionWhenDnaIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> validator.validate(null));

        assertEquals("DNA sequence cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDnaIsEmpty() {
        String[] emptyDna = {};

        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> validator.validate(emptyDna));

        assertEquals("DNA sequence cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenMatrixIsNotSquare() {
        String[] nonSquareDna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT"
        };

        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> validator.validate(nonSquareDna));

        assertEquals("DNA matrix must be NxN (square)", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenRowLengthDoesNotMatchMatrixSize() {
        String[] invalidDna = {
            "ATGCGA",
            "CAGTGC",
            "TTAT",  // Wrong length
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };

        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> validator.validate(invalidDna));

        assertTrue(exception.getMessage().contains("must be NxN"));
    }

    @Test
    void shouldThrowExceptionWhenRowIsNull() {
        String[] dnaWithNullRow = {
            "ATGCGA",
            null,
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };

        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> validator.validate(dnaWithNullRow));

        assertEquals("DNA row cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenContainsInvalidCharacters() {
        String[] dnaWithInvalidChars = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGXAGG",  // X is not a valid nitrogenous base
            "CCCCTA",
            "TCACTG"
        };

        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> validator.validate(dnaWithInvalidChars));

        assertTrue(exception.getMessage().contains("Invalid character"));
    }

    @Test
    void shouldThrowExceptionWhenContainsLowercaseLetters() {
        String[] dnaWithLowercase = {
            "ATGCGA",
            "CAGTGC",
            "TTaTGT",  // lowercase 'a' is not allowed
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };

        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> validator.validate(dnaWithLowercase));

        assertTrue(exception.getMessage().contains("Invalid character"));
    }

    @Test
    void shouldAcceptMinimum4x4Matrix() {
        String[] minSizeDna = {
            "ATGC",
            "CAGT",
            "TTAT",
            "AGAC"
        };

        assertDoesNotThrow(() -> validator.validate(minSizeDna));
    }

    @Test
    void shouldThrowExceptionWhenMatrixIsSmallerThan4x4() {
        String[] tooSmallDna = {
            "ATG",
            "CAG",
            "TTA"
        };

        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> validator.validate(tooSmallDna));

        assertEquals("DNA matrix must be at least 4x4 to detect sequences", exception.getMessage());
    }

    @Test
    void shouldAcceptOnlyATCGCharacters() {
        String[] dnaWithAllBases = {
            "AAAA",
            "TTTT",
            "CCCC",
            "GGGG"
        };

        assertDoesNotThrow(() -> validator.validate(dnaWithAllBases));
    }
}
