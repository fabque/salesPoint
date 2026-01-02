package com.ar.sales.point.infrastructure.persistance;

import static org.junit.jupiter.api.Assertions.*;

import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.domain.model.SalePointCost;
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
        assertEquals(10L, fetched.getSalePointOrigin().id());
        assertEquals(20L, fetched.getSalePointDestination().id());

        List<SalePointCost> all = adapter.findAll();
        assertTrue(all.size() >= 1);

        // Ahora verificamos deleteById (cubrimos el método faltante)
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
        SalePointCost toUpdate = new SalePointCost(saved.getId(), new SalePoint(3L, "O2"), new SalePoint(4L, "D2"), 9.99);
        SalePointCost updated = adapter.save(toUpdate);

        assertEquals(saved.getId(), updated.getId());
        assertEquals(9.99, updated.getCost());
        assertEquals(3L, updated.getSalePointOrigin().id());
        assertEquals(4L, updated.getSalePointDestination().id());
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
        SalePointCost toSave = new SalePointCost(fakeId, new SalePoint(11L, "OX"), new SalePoint(22L, "DX"), 42.0);

        // No debe lanzar excepción
        SalePointCost result = adapter.save(toSave);
        assertNotNull(result.getId());

        // El registro persistido se puede recuperar usando el id retornado
        SalePointCost persisted = adapter.findById(result.getId());
        assertNotNull(persisted);
        assertEquals(42.0, persisted.getCost());
        assertEquals(11L, persisted.getSalePointOrigin().id());
        assertEquals(22L, persisted.getSalePointDestination().id());

        // Cleanup
        adapter.deleteById(result.getId());
        assertNull(adapter.findById(result.getId()));
    }
}
