package com.ar.sales.point.infrastructure.controller;

import com.ar.sales.point.infrastructure.controller.dto.SalePointCostRequest;
import com.ar.sales.point.infrastructure.controller.dto.SalePointCostResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SalePointCostControllerE2eTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/salepoint-costs";
    }

    @Test
    void create_get_update_delete_flow() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create
        SalePointCostRequest createReq = new SalePointCostRequest();
        createReq.setOriginId(100L);
        createReq.setDestinationId(200L);
        createReq.setCost(12.34);

        ResponseEntity<SalePointCostResponse> createResp = restTemplate.postForEntity(baseUrl(), new HttpEntity<>(createReq, headers), SalePointCostResponse.class);
        assertEquals(200, createResp.getStatusCodeValue());
        assertNotNull(createResp.getBody());
        Long createdId = createResp.getBody().getId();
        assertNotNull(createdId);

        // Get
        ResponseEntity<SalePointCostResponse> getResp = restTemplate.getForEntity(baseUrl() + "/" + createdId, SalePointCostResponse.class);
        assertEquals(200, getResp.getStatusCodeValue());
        assertNotNull(getResp.getBody());
        assertEquals(100L, getResp.getBody().getOriginId());
        assertEquals(200L, getResp.getBody().getDestinationId());
        assertEquals(12.34, getResp.getBody().getCost());

        // Update
        SalePointCostRequest updateReq = new SalePointCostRequest();
        updateReq.setOriginId(101L);
        updateReq.setDestinationId(201L);
        updateReq.setCost(99.99);

        ResponseEntity<SalePointCostResponse> updateResp = restTemplate.exchange(baseUrl() + "/" + createdId, HttpMethod.PUT, new HttpEntity<>(updateReq, headers), SalePointCostResponse.class);
        assertEquals(200, updateResp.getStatusCodeValue());
        assertNotNull(updateResp.getBody());
        assertEquals(createdId, updateResp.getBody().getId());
        assertEquals(101L, updateResp.getBody().getOriginId());
        assertEquals(201L, updateResp.getBody().getDestinationId());
        assertEquals(99.99, updateResp.getBody().getCost());

        // Delete
        ResponseEntity<Void> deleteResp = restTemplate.exchange(baseUrl() + "/" + createdId, HttpMethod.DELETE, null, Void.class);
        assertTrue(deleteResp.getStatusCode().is2xxSuccessful());

        // After delete, GET should return 404 and an error body handled by ControllerAdvice
        ResponseEntity<String> getAfterDelete = restTemplate.getForEntity(baseUrl() + "/" + createdId, String.class);
        assertEquals(404, getAfterDelete.getStatusCodeValue());
        assertNotNull(getAfterDelete.getBody());
        assertTrue(getAfterDelete.getBody().contains("SalePointCost with id"));
    }
}
