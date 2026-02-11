package com.example.stats_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public record StatsResponse(
    @JsonProperty("count_mutant_dna")
    long countMutantDna,

    @JsonProperty("count_human_dna")
    long countHumanDna,

    @JsonProperty("ratio")
    double ratio
) implements Serializable {}
