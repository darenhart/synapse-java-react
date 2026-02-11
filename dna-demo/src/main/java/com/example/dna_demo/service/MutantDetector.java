package com.example.dna_demo.service;

import com.example.dna_demo.validator.DnaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4;
    private static final int REQUIRED_SEQUENCES = 2;

    private final DnaValidator dnaValidator;

    /**
     * Determines if a DNA sequence belongs to a mutant
     * @param dna Array of strings representing DNA sequence
     * @return true if mutant (more than one sequence found), false otherwise
     */
    public boolean isMutant(String[] dna) {
        dnaValidator.validate(dna);

        int n = dna.length;
        int sequencesFound = 0;

        // Check horizontal sequences
        sequencesFound += countHorizontalSequences(dna, n);
        if (sequencesFound >= REQUIRED_SEQUENCES) {
            return true;
        }

        // Check vertical sequences
        sequencesFound += countVerticalSequences(dna, n);
        if (sequencesFound >= REQUIRED_SEQUENCES) {
            return true;
        }

        // Check diagonal sequences (top-left to bottom-right)
        sequencesFound += countDiagonalTLBRSequences(dna, n);
        if (sequencesFound >= REQUIRED_SEQUENCES) {
            return true;
        }

        // Check diagonal sequences (top-right to bottom-left)
        sequencesFound += countDiagonalTRBLSequences(dna, n);

        return sequencesFound >= REQUIRED_SEQUENCES;
    }

    private int countHorizontalSequences(String[] dna, int n) {
        int count = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col <= n - SEQUENCE_LENGTH; col++) {
                if (hasSequence(dna[row], col)) {
                    count++;
                    if (count >= REQUIRED_SEQUENCES) {
                        return count;
                    }
                    // Skip remaining positions in this row to avoid counting overlapping sequences
                    break;
                }
            }
        }
        return count;
    }

    private boolean hasSequence(String str, int startPos) {
        char firstChar = str.charAt(startPos);
        for (int i = 1; i < SEQUENCE_LENGTH; i++) {
            if (str.charAt(startPos + i) != firstChar) {
                return false;
            }
        }
        return true;
    }

    private int countVerticalSequences(String[] dna, int n) {
        int count = 0;
        for (int col = 0; col < n; col++) {
            for (int row = 0; row <= n - SEQUENCE_LENGTH; row++) {
                if (hasVerticalSequence(dna, row, col)) {
                    count++;
                    if (count >= REQUIRED_SEQUENCES) {
                        return count;
                    }
                    // Skip remaining positions in this column to avoid counting overlapping sequences
                    break;
                }
            }
        }
        return count;
    }

    private boolean hasVerticalSequence(String[] dna, int startRow, int col) {
        char firstChar = dna[startRow].charAt(col);
        for (int i = 1; i < SEQUENCE_LENGTH; i++) {
            if (dna[startRow + i].charAt(col) != firstChar) {
                return false;
            }
        }
        return true;
    }

    private int countDiagonalTLBRSequences(String[] dna, int n) {
        int count = 0;

        // Check diagonals starting from first row
        for (int col = 0; col <= n - SEQUENCE_LENGTH; col++) {
            count += countDiagonalTLBRFromPosition(dna, 0, col, n);
            if (count >= REQUIRED_SEQUENCES) {
                return count;
            }
        }

        // Check diagonals starting from first column (excluding [0,0] already checked)
        for (int row = 1; row <= n - SEQUENCE_LENGTH; row++) {
            count += countDiagonalTLBRFromPosition(dna, row, 0, n);
            if (count >= REQUIRED_SEQUENCES) {
                return count;
            }
        }

        return count;
    }

    private int countDiagonalTLBRFromPosition(String[] dna, int startRow, int startCol, int n) {
        int count = 0;
        int row = startRow;
        int col = startCol;

        while (row <= n - SEQUENCE_LENGTH && col <= n - SEQUENCE_LENGTH) {
            if (hasDiagonalTLBRSequence(dna, row, col)) {
                count++;
                if (count >= REQUIRED_SEQUENCES) {
                    return count;
                }
                // Skip to avoid overlapping
                row += SEQUENCE_LENGTH;
                col += SEQUENCE_LENGTH;
            } else {
                row++;
                col++;
            }
        }

        return count;
    }

    private boolean hasDiagonalTLBRSequence(String[] dna, int startRow, int startCol) {
        char firstChar = dna[startRow].charAt(startCol);
        for (int i = 1; i < SEQUENCE_LENGTH; i++) {
            if (dna[startRow + i].charAt(startCol + i) != firstChar) {
                return false;
            }
        }
        return true;
    }

    private int countDiagonalTRBLSequences(String[] dna, int n) {
        int count = 0;

        // Check diagonals starting from first row
        for (int col = SEQUENCE_LENGTH - 1; col < n; col++) {
            count += countDiagonalTRBLFromPosition(dna, 0, col, n);
            if (count >= REQUIRED_SEQUENCES) {
                return count;
            }
        }

        // Check diagonals starting from last column (excluding [0, n-1] already checked)
        for (int row = 1; row <= n - SEQUENCE_LENGTH; row++) {
            count += countDiagonalTRBLFromPosition(dna, row, n - 1, n);
            if (count >= REQUIRED_SEQUENCES) {
                return count;
            }
        }

        return count;
    }

    private int countDiagonalTRBLFromPosition(String[] dna, int startRow, int startCol, int n) {
        int count = 0;
        int row = startRow;
        int col = startCol;

        while (row <= n - SEQUENCE_LENGTH && col >= SEQUENCE_LENGTH - 1) {
            if (hasDiagonalTRBLSequence(dna, row, col)) {
                count++;
                if (count >= REQUIRED_SEQUENCES) {
                    return count;
                }
                // Skip to avoid overlapping
                row += SEQUENCE_LENGTH;
                col -= SEQUENCE_LENGTH;
            } else {
                row++;
                col--;
            }
        }

        return count;
    }

    private boolean hasDiagonalTRBLSequence(String[] dna, int startRow, int startCol) {
        char firstChar = dna[startRow].charAt(startCol);
        for (int i = 1; i < SEQUENCE_LENGTH; i++) {
            if (dna[startRow + i].charAt(startCol - i) != firstChar) {
                return false;
            }
        }
        return true;
    }
}
