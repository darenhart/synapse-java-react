package com.example.dna_demo.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DnaVerifiedEventTest {

    @Test
    void testConstructorWithoutTimestamp() {
        // Given
        String dnaHash = "abc123";
        boolean isMutant = true;

        // When
        DnaVerifiedEvent event = new DnaVerifiedEvent(dnaHash, isMutant);

        // Then
        assertEquals(dnaHash, event.getDnaHash());
        assertTrue(event.isMutant());
        assertTrue(event.getTimestamp() > 0);
    }

    @Test
    void testConstructorWithTimestamp() {
        // Given
        String dnaHash = "xyz789";
        boolean isMutant = false;
        long timestamp = 1234567890L;

        // When
        DnaVerifiedEvent event = new DnaVerifiedEvent(dnaHash, isMutant, timestamp);

        // Then
        assertEquals(dnaHash, event.getDnaHash());
        assertFalse(event.isMutant());
        assertEquals(timestamp, event.getTimestamp());
    }

    @Test
    void testNoArgsConstructor() {
        // When
        DnaVerifiedEvent event = new DnaVerifiedEvent();

        // Then
        assertNotNull(event);
        assertNull(event.getDnaHash());
        assertFalse(event.isMutant());
        assertEquals(0, event.getTimestamp());
    }

    @Test
    void testSetters() {
        // Given
        DnaVerifiedEvent event = new DnaVerifiedEvent();

        // When
        event.setDnaHash("hash123");
        event.setMutant(true);
        event.setTimestamp(9999999L);

        // Then
        assertEquals("hash123", event.getDnaHash());
        assertTrue(event.isMutant());
        assertEquals(9999999L, event.getTimestamp());
    }

    @Test
    void testTimestampAutoGeneration() {
        // Given
        long beforeTimestamp = System.currentTimeMillis();

        // When
        DnaVerifiedEvent event = new DnaVerifiedEvent("hash", true);

        // Then
        long afterTimestamp = System.currentTimeMillis();
        assertTrue(event.getTimestamp() >= beforeTimestamp);
        assertTrue(event.getTimestamp() <= afterTimestamp);
    }
}
