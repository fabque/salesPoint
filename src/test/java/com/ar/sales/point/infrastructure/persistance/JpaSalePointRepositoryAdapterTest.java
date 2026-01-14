package com.ar.sales.point.infrastructure.persistance;

import static org.junit.jupiter.api.Assertions.*;

import com.ar.sales.point.domain.model.SalePoint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@DataJpaTest
@Import(JpaSalePointRepositoryAdapter.class)
public class JpaSalePointRepositoryAdapterTest {

    @Autowired
    private JpaSalePointRepositoryAdapter adapter;

    @Test
    void save_and_find() {
        SalePoint sp = new SalePoint(1L, "H2 SP");

        SalePoint saved = assertDoesNotThrow(() -> adapter.save(sp), "save should not throw ConflictException");
        assertNotNull(saved);
        assertNotNull(saved.getId());

        SalePoint fetched = assertDoesNotThrow(() -> adapter.findById(saved.getId()), "findById should not throw ResourceNotFoundException");
        assertNotNull(fetched);
        assertEquals("H2 SP", fetched.getName());

        List<SalePoint> all = adapter.findAll();
        assertFalse(all.isEmpty(), "findAll should return at least one element");
    }
}
