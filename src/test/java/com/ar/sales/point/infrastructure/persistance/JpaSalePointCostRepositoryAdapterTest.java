package com.ar.sales.point.infrastructure.persistance;

import static org.junit.jupiter.api.Assertions.*;

import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.domain.model.SalePointCost;
import com.ar.sales.point.infrastructure.persistance.repositories.SpringDataSalePointCostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@DataJpaTest
@Import(JpaSalePointCostRepositoryAdapter.class)
public class JpaSalePointCostRepositoryAdapterTest {

    @Autowired
    private SpringDataSalePointCostRepository springDataSalePointCostRepository;

    @Autowired
    private JpaSalePointCostRepositoryAdapter adapter;

    @Test
    void save_and_find() {
        SalePointCost spc = new SalePointCost(null, new SalePoint(10L, "Origin"), new SalePoint(20L, "Destination"), 15.5);
        SalePointCost saved = adapter.save(spc);
        assertNotNull(saved.getId());

        SalePointCost fetched = adapter.findById(saved.getId());
        assertNotNull(fetched);
        assertEquals(15.5, fetched.getCost());
        assertNotNull(fetched.getSalePointOrigin());
        assertNotNull(fetched.getSalePointDestination());
        assertEquals(10L, fetched.getSalePointOrigin().getId());
        assertEquals(20L, fetched.getSalePointDestination().getId());

        List<SalePointCost> all = adapter.findAll();
        assertTrue(all.size() >= 1);

        adapter.deleteById(saved.getId());
        SalePointCost afterDelete = adapter.findById(saved.getId());
        assertNull(afterDelete);
    }

    @Test
    void update_existing_record_updates_values() {
        // Guardar original
        SalePointCost spc = new SalePointCost(null, new SalePoint(1L, "O"), new SalePoint(2L, "D"), 5.0);
        SalePointCost saved = adapter.save(spc);
        assertNotNull(saved.getId());

        // Modificar cost y origen/destino
        SalePointCost toUpdate = new SalePointCost(saved.getId(), new SalePoint(1L, "O"), new SalePoint(2L, "D"), 9.99);
        SalePointCost updated = adapter.save(toUpdate);

        assertEquals(saved.getId(), updated.getId());
        assertEquals(9.99, updated.getCost());
        assertEquals(1L, updated.getSalePointOrigin().getId());
        assertEquals(2L, updated.getSalePointDestination().getId());
    }

    @Test
    void save_with_null_origin_or_destination_and_delete_nonexistent() {
        // Guardar con origin y destination nulos
        SalePointCost spcNulls = new SalePointCost(null, null, null, 1.23);
        SalePointCost saved = adapter.save(spcNulls);
        assertNotNull(saved.getId());
        assertNull(saved.getSalePointOrigin());
        assertNull(saved.getSalePointDestination());

        // Intentar borrar un id que no existe no debe lanzar excepción
        Long nonexistent = 999999L;
        adapter.deleteById(nonexistent); // no exception expected

        // El registro previamente guardado sigue existiendo hasta que lo borremos
        SalePointCost fetched = adapter.findById(saved.getId());
        assertNotNull(fetched);

        // Limpieza
        adapter.deleteById(saved.getId());
        assertNull(adapter.findById(saved.getId()));
    }

    @Test
    void save_with_nonexistent_id_behaves_gracefully() {
        // Intentar "actualizar" un id que no existe en la BD
        Long fakeId = 123456789L;
        SalePointCost toSave = SalePointCost.builder().salePointOrigin(new SalePoint(11L, "OX")).salePointDestination(new SalePoint(22L, "DX")).cost( 42.0).build();

        // No debe lanzar excepción
        SalePointCost result = adapter.save(toSave);
        assertNotNull(result.getId());

        // El registro persistido se puede recuperar usando el id retornado
        SalePointCost persisted = adapter.findById(result.getId());
        assertNotNull(persisted);
        assertEquals(42.0, persisted.getCost());
        assertEquals(11L, persisted.getSalePointOrigin().getId());
        assertEquals(22L, persisted.getSalePointDestination().getId());

        // Cleanup
        adapter.deleteById(result.getId());
        assertNull(adapter.findById(result.getId()));
    }
}
