package com.example.stats_service.controller;

import com.example.stats_service.dto.StatsResponse;
import com.example.stats_service.service.StatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StatsControllerIntegrationTest {

    @Autowired
    private StatsController controller;

    @Autowired
    private StatsService service;

    @Test
    void getStats_shouldReturnStats() {
        // When
        ResponseEntity<StatsResponse> responseEntity = controller.getStats();

        // Then
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCode().value());
        StatsResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertNotNull(response.countMutantDna());
        assertNotNull(response.countHumanDna());
        assertTrue(response.ratio() >= 0);
    }
}
