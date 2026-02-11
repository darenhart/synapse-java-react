package com.example.dna_demo.dto;

import jakarta.validation.constraints.NotNull;

public record DnaRequest(
    @NotNull(message = "DNA sequence cannot be null")
    String[] dna
) {}
