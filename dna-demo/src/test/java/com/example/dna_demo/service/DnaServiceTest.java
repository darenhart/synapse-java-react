package com.example.dna_demo.service;

import com.example.dna_demo.entity.DnaRecord;
import com.example.dna_demo.event.DnaEventPublisher;
import com.example.dna_demo.event.DnaVerifiedEvent;
import com.example.dna_demo.repository.DnaRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class DnaServiceTest {

    @Mock
    private MutantDetector mutantDetector;

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @Mock
    private DnaEventPublisher dnaEventPublisher;

    @InjectMocks
    private DnaService dnaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveNewMutantDnaAndPublishEvent() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};

        when(dnaRecordRepository.existsByDnaHash(anyString())).thenReturn(false);
        when(mutantDetector.isMutant(any())).thenReturn(true);
        when(dnaRecordRepository.save(any())).thenReturn(new DnaRecord());

        boolean result = dnaService.verifyAndSave(dna);

        assertTrue(result);
        verify(dnaRecordRepository).save(any(DnaRecord.class));
        verify(dnaEventPublisher).publish(any(DnaVerifiedEvent.class));
    }

    @Test
    void shouldSaveNewHumanDnaAndPublishEvent() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"};

        when(dnaRecordRepository.existsByDnaHash(anyString())).thenReturn(false);
        when(mutantDetector.isMutant(any())).thenReturn(false);
        when(dnaRecordRepository.save(any())).thenReturn(new DnaRecord());

        boolean result = dnaService.verifyAndSave(dna);

        assertFalse(result);
        verify(dnaRecordRepository).save(any(DnaRecord.class));
        verify(dnaEventPublisher).publish(any(DnaVerifiedEvent.class));
    }

    @Test
    void shouldReturnCachedResultForDuplicateDna() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        DnaRecord existingRecord = new DnaRecord();
        existingRecord.setIsMutant(true);

        when(dnaRecordRepository.existsByDnaHash(anyString())).thenReturn(true);
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.of(existingRecord));

        boolean result = dnaService.verifyAndSave(dna);

        assertTrue(result);
        verify(mutantDetector, never()).isMutant(any());
        verify(dnaRecordRepository, never()).save(any());
        verify(dnaEventPublisher, never()).publish(any());
    }

    @Test
    void shouldNotPublishEventForDuplicateDna() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        DnaRecord existingRecord = new DnaRecord();
        existingRecord.setIsMutant(true);

        when(dnaRecordRepository.existsByDnaHash(anyString())).thenReturn(true);
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.of(existingRecord));

        // Call twice
        dnaService.verifyAndSave(dna);
        dnaService.verifyAndSave(dna);

        verify(dnaEventPublisher, never()).publish(any());
    }

    @Test
    void shouldGetCachedResultReturnsNullWhenNotFound() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};

        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());

        Boolean result = dnaService.getCachedResult(dna);

        assertNull(result);
    }

    @Test
    void shouldGetCachedResultReturnsValueWhenFound() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        DnaRecord record = new DnaRecord();
        record.setIsMutant(true);

        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.of(record));

        Boolean result = dnaService.getCachedResult(dna);

        assertTrue(result);
    }
}
