package com.ar.sales.point.infrastructure.controller;

import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class RestExceptionControllerAdviceTest {

    @Test
    void restExceptionHandlerReturnsJsonAndStatus404() {
        RestExceptionHandler advice = new RestExceptionHandler();
        ResponseEntity<String> resp = advice.handleNotFound(new ResourceNotFoundException("not found"));

        assertThat(resp.getStatusCodeValue()).isEqualTo(404);
        assertThat(resp.getHeaders().getContentType().toString()).contains("application/json");
        assertThat(resp.getBody()).contains("\"error\":\"not found\"");
    }
}
