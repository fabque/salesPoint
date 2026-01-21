package com.ar.sales.point.infrastructure.persistence;

import com.ar.sales.point.domain.model.Accreditation;
import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;
import com.ar.sales.point.infrastructure.persistence.postgres.AccreditationEntity;
import com.ar.sales.point.infrastructure.persistence.postgres.repositories.SpringDataAccreditationRepository;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JpaAccreditationtRepositoryAdapterTest {

    @Test
    void save_and_find_and_list_flow() throws ResourceNotFoundException {
        SpringDataAccreditationRepository springRepo = mock(SpringDataAccreditationRepository.class);
        JpaAccreditationtRepositoryAdapter adapter = new JpaAccreditationtRepositoryAdapter(springRepo);

        SalePoint sp = new SalePoint(1L, "SP1");
        AccreditationDomainBuilder builder = new AccreditationDomainBuilder();

        Accreditation input = new Accreditation(sp, 50.0, new Date());
        AccreditationEntity savedEntity = new AccreditationEntity(1L, 1L, sp.getName(), 50.0, input.getAcreditationDate());

        when(springRepo.save(any(AccreditationEntity.class))).thenReturn(savedEntity);
        when(springRepo.findById(1L)).thenReturn(Optional.of(savedEntity));
        when(springRepo.findAll()).thenReturn(List.of(savedEntity));
        when(springRepo.findBySalePointId(1L)).thenReturn(List.of(savedEntity));

        Accreditation saved = adapter.save(input);
        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        assertEquals(50.0, saved.getAmount());

        Accreditation fetched = adapter.findById(1L);
        assertNotNull(fetched);
        assertEquals(1L, fetched.getId());

        List<Accreditation> all = adapter.findAll();
        assertEquals(1, all.size());

        List<Accreditation> bySalePoint = adapter.findBySalePointId(1L);
        assertEquals(1, bySalePoint.size());

        verify(springRepo, times(1)).save(any(AccreditationEntity.class));
    }

    @Test
    void findById_when_not_found_should_throw() {
        SpringDataAccreditationRepository springRepo = mock(SpringDataAccreditationRepository.class);
        JpaAccreditationtRepositoryAdapter adapter = new JpaAccreditationtRepositoryAdapter(springRepo);

        when(springRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adapter.findById(99L));
    }

    // Small helper builder (local) to avoid importing test utilities
    static class AccreditationDomainBuilder {
        Accreditation build(Long id, Long spId, String spName, Double amount, Date d) {
            SalePoint sp = new SalePoint(spId, spName);
            return new Accreditation(id, sp, spName, amount, d);
        }
    }
}
