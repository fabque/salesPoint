package com.ar.sales.point.infrastructure.persistance;

import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.domain.model.SalePointCost;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JpaSalePointCostRepositoryAdapter.class)
public class JpaSalePointCostRepositoryAdapterConcurrencyTest {

    @Autowired
    private JpaSalePointCostRepositoryAdapter adapter;

    @Test
    void concurrent_inserts_produce_unique_ids() throws InterruptedException, ExecutionException {
        int threads = 8;
        int perThread = 10;
        ExecutorService ex = Executors.newFixedThreadPool(threads);

        List<Callable<Long>> tasks = IntStream.range(0, threads * perThread).mapToObj(i -> (Callable<Long>) () -> {
            SalePointCost spc = new SalePointCost(null, new SalePoint((long) i, "O" + i), new SalePoint((long) (i + 1000), "D" + i), (double) i);
            SalePointCost saved = adapter.save(spc);
            return saved.getId();
        }).collect(Collectors.toList());

        List<Future<Long>> futures = ex.invokeAll(tasks);
        Set<Long> ids = new HashSet<>();
        for (Future<Long> f : futures) {
            Long id = f.get();
            assertNotNull(id);
            ids.add(id);
        }

        // Expect all ids unique
        assertEquals(threads * perThread, ids.size());

        // Cleanup
        ids.forEach(adapter::deleteById);
        ex.shutdown();
    }
}

