package com.ar.sales.point.application.service;

import com.ar.sales.point.application.port.out.AccreditationRepositoryPort;
import com.ar.sales.point.domain.model.Accreditation;
import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccreditationServiceTest {

    @Test
    void create_and_get_and_list_flow() throws ResourceNotFoundException {
        AccreditationRepositoryPort repo = mock(AccreditationRepositoryPort.class);
        SalePointService salePointService = mock(SalePointService.class);
        AccreditationService service = new AccreditationService(salePointService, repo);

        SalePoint sp = new SalePoint(1L, "SP1");
        Accreditation input = new Accreditation(sp, 100.0, new Date());
        Accreditation saved = new Accreditation(1L, sp, sp.getName(), 100.0, input.getAcreditationDate());

        when(salePointService.getSalePointById(1L)).thenReturn(sp);
        when(repo.save(any(Accreditation.class))).thenReturn(saved);
        when(repo.findById(1L)).thenReturn(saved);
        when(repo.findAll()).thenReturn(List.of(saved));
        when(repo.findBySalePointId(1L)).thenReturn(List.of(saved));

        Accreditation created = service.createAccreditation(input);
        assertNotNull(created);
        assertEquals(1L, created.getId());

        Accreditation fetched = service.getAccreditationById(1L);
        assertNotNull(fetched);
        assertEquals(100.0, fetched.getAmount());

        List<Accreditation> all = service.getAllAccreditations();
        assertEquals(1, all.size());

        List<Accreditation> bySalePoint = service.getAccreditationsBySalePointId(1L);
        assertEquals(1, bySalePoint.size());

        verify(repo, times(1)).save(any(Accreditation.class));
    }

    @Test
    void create_when_salePoint_not_exists_should_throw() throws ResourceNotFoundException {
        AccreditationRepositoryPort repo = mock(AccreditationRepositoryPort.class);
        SalePointService salePointService = mock(SalePointService.class);
        AccreditationService service = new AccreditationService(salePointService, repo);

        SalePoint sp = new SalePoint(99L, "SPX");
        Accreditation input = new Accreditation(sp, 50.0, new Date());

        when(salePointService.getSalePointById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> service.createAccreditation(input));
    }
}
