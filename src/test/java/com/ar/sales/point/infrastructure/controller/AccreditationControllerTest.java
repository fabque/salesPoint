package com.ar.sales.point.infrastructure.controller;

import com.ar.sales.point.application.service.AccreditationService;
import com.ar.sales.point.domain.model.Accreditation;
import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.infrastructure.controller.dto.AccreditationRequest;
import com.ar.sales.point.infrastructure.controller.dto.AccreditationResponse;
import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccreditationControllerTest {

    @Test
    void create_success_and_not_found() {
        AccreditationService service = mock(AccreditationService.class);
        AccreditationController controller = new AccreditationController(service);

        AccreditationRequest req = new AccreditationRequest(1L, "SP1", 100.0, new Date());
        SalePoint sp = new SalePoint(1L, "SP1");
        Accreditation acc = new Accreditation(sp, 100.0, req.acreditationDate());

        try {
            when(service.createAccreditation(any(Accreditation.class))).thenReturn(new Accreditation(1L, sp, sp.getName(), 100.0, req.acreditationDate()));
        } catch (ResourceNotFoundException e) {
            fail("should not throw");
        }

        ResponseEntity<?> res = controller.createAccreditation(req);
        assertEquals(201, res.getStatusCodeValue());
        assertTrue(res.getBody() instanceof AccreditationResponse);

        // simulate service throws ResourceNotFoundException
        try {
            when(service.createAccreditation(any(Accreditation.class))).thenThrow(new ResourceNotFoundException("SalePoint not found"));
        } catch (ResourceNotFoundException e) {
            // noop
        }

        ResponseEntity<?> res2 = controller.createAccreditation(req);
        assertEquals(404, res2.getStatusCodeValue());
    }

    @Test
    void get_by_id_success_and_not_found() {
        AccreditationService service = mock(AccreditationService.class);
        AccreditationController controller = new AccreditationController(service);

        SalePoint sp = new SalePoint(1L, "SP1");
        Accreditation acc = new Accreditation(1L, sp, sp.getName(), 100.0, new Date());

        try {
            when(service.getAccreditationById(1L)).thenReturn(acc);
        } catch (ResourceNotFoundException e) {
            fail("should not throw");
        }

        ResponseEntity<?> res = controller.getAccreditationById(1L);
        assertEquals(200, res.getStatusCodeValue());
        assertTrue(res.getBody() instanceof AccreditationResponse);

        try {
            when(service.getAccreditationById(2L)).thenThrow(new ResourceNotFoundException("Accreditation not found"));
        } catch (ResourceNotFoundException e) {
            // noop
        }

        ResponseEntity<?> res2 = controller.getAccreditationById(2L);
        assertEquals(404, res2.getStatusCodeValue());
    }

    @Test
    void list_all_and_by_salepoint() {
        AccreditationService service = mock(AccreditationService.class);
        AccreditationController controller = new AccreditationController(service);

        SalePoint sp = new SalePoint(1L, "SP1");
        Accreditation acc = new Accreditation(1L, sp, sp.getName(), 100.0, new Date());

        when(service.getAllAccreditations()).thenReturn(List.of(acc));
        when(service.getAccreditationsBySalePointId(1L)).thenReturn(List.of(acc));

        ResponseEntity<?> all = controller.getAllAccreditations();
        assertEquals(200, all.getStatusCodeValue());
        assertTrue(all.getBody() instanceof java.util.List);

        ResponseEntity<?> bySp = controller.getAccreditationBySalepointId(1L);
        assertEquals(200, bySp.getStatusCodeValue());
        assertTrue(bySp.getBody() instanceof java.util.List);
    }
}
