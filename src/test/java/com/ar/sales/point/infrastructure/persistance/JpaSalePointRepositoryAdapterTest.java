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
    private SpringDataSalePointRepository springDataSalePointRepository;

    @Autowired
    private JpaSalePointRepositoryAdapter adapter;

    @Test
    void save_and_find() {
        SalePoint sp = new SalePoint(1L, "H2 SP");
        SalePoint saved = adapter.save(sp);
        assertNotNull(saved.id());
        SalePoint fetched = adapter.findById(saved.id());
        assertNotNull(fetched);
        assertEquals("H2 SP", fetched.name());

        List<SalePoint> all = adapter.findAll();
        assertTrue(all.size() >= 1);
    }
}

