package com.example.dna_demo.service;

import com.example.dna_demo.validator.DnaValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

class MutantDetectorTest {

    private MutantDetector mutantDetector;

    @Mock
    private DnaValidator dnaValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mutantDetector = new MutantDetector(dnaValidator);
        // Mock validator to avoid validation logic in these tests
        doNothing().when(dnaValidator).validate(any());
    }

    @Test
    void shouldReturnTrueForMutantWithMultipleHorizontalSequences() {
        String[] dna = {
            "AAAA",
            "TTTT",
            "CCGG",
            "GGCC"
        };

        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void shouldReturnTrueForMutantWithMultipleVerticalSequences() {
        String[] dna = {
            "ATGC",
            "ATGC",
            "ATGC",
            "ATGC"
        };

        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void shouldReturnTrueForMutantWithDiagonalSequences() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };

        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void shouldReturnTrueForMutantWithHorizontalAndVerticalSequences() {
        // AAAA horizontal in row 0, and CCCC vertical in column 0
        String[] dna = {
            "AAAA",
            "CTGC",
            "CTGC",
            "CTGC",
            "CTGC"
        };

        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void shouldReturnTrueForMutantWithTopLeftToBottomRightDiagonal() {
        // AAAA diagonal from (0,0) to (3,3): A,A,A,A
        // CCCC horizontal in row 0 starting at position 4 (in a 5x5 matrix)
        String[] dna = {
            "ACCCC",
            "CATGG",
            "GCATG",
            "GGCAT",
            "GGGCA"
        };

        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void shouldReturnTrueForMutantWithTopRightToBottomLeftDiagonal() {
        // AAAA diagonal from (0,4) to (3,1): positions (0,4)=A, (1,3)=A, (2,2)=A, (3,1)=A
        // CCCC horizontal in row 0 at positions 0-3
        String[] dna = {
            "CCCCA",
            "TGTAT",
            "GTAAT",
            "TAGCT",
            "GTGCT"
        };

        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void shouldReturnFalseForNonMutantWithOnlyOneSequence() {
        String[] dna = {
            "AAAA",
            "CTGC",
            "TGCA",
            "GTCA"
        };

        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void shouldReturnFalseForNonMutantWithNoSequences() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATTT",
            "AGACGG",
            "GCGTCA",
            "TCACTG"
        };

        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void shouldReturnFalseForNonMutantWithSequenceOfThree() {
        String[] dna = {
            "AAAG",
            "CTGC",
            "TGCA",
            "GTCA"
        };

        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void shouldHandleMinimum4x4Matrix() {
        String[] dna = {
            "AAAA",
            "AAAA",
            "TGCA",
            "GTCA"
        };

        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void shouldHandleLargeMatrix() {
        String[] dna = {
            "ATGCGAATGC",
            "CAGTGCCAGT",
            "TTATGTAAAT",
            "AGAAGGATGC",
            "CCCCTACCCC",
            "TCACTGTCAC",
            "ATGCGAATGC",
            "CAGTGCCAGT",
            "TTATGTTTAT",
            "AGAAGGATGC"
        };

        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void shouldCountFiveConsecutiveAsOneSequence() {
        String[] dna = {
            "AAAAA",
            "TTTTT",
            "CGCGC",
            "GCGCG",
            "AAAAA"
        };

        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void shouldHandleDiagonalSequenceInMiddleOfMatrix() {
        // CCCC horizontal in row 4
        // AAAA diagonal from (1,1) to (4,4): positions (1,1)=A, (2,2)=A, (3,3)=A, (4,4)=A
        String[] dna = {
            "TGCAGT",
            "CACGTC",
            "TTACGT",
            "AGTAGA",
            "CCCCAA",
            "TCACTA"
        };

        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void shouldReturnTrueForExampleMutantCase() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };

        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void shouldReturnFalseForExampleNonMutantCase() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATTT",
            "AGACGG",
            "GCGTCA",
            "TCACTG"
        };

        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void shouldHandleAllSameCharacter() {
        String[] dna = {
            "AAAA",
            "AAAA",
            "AAAA",
            "AAAA"
        };

        // This should have many sequences (horizontal, vertical, diagonal)
        assertTrue(mutantDetector.isMutant(dna));
    }
}
