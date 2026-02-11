package com.example.dna_demo.controller;

import com.example.dna_demo.dto.DnaRequest;
import com.example.dna_demo.service.DnaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mutant")
@RequiredArgsConstructor
public class MutantController {

    private final DnaService dnaService;

    @PostMapping("/")
    public ResponseEntity<Void> checkMutant(@Valid @RequestBody DnaRequest request) {
        boolean isMutant = dnaService.verifyAndSave(request.dna());

        if (isMutant) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
