package hu.udinfopark.optimistic.lock;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import hu.udinfopark.jpa.optimistic.JpaOptimisticLockingApplication;
import hu.udinfopark.jpa.optimistic.dto.OptimisticDTO;
import hu.udinfopark.jpa.optimistic.repository.OptimisticRepository;
import hu.udinfopark.jpa.optimistic.repository.entity.Optimistic;
import hu.udinfopark.jpa.optimistic.service.OptimisticService;

@SpringBootTest(classes = JpaOptimisticLockingApplication.class)
@Import(TestcontainersConfiguration.class)
@Testcontainers
class JpaOptimisticLockingIntegrationTest {

    @Autowired
    private OptimisticService optimisticService;

    @Autowired
    private OptimisticRepository optimisticRepository;

    private final OptimisticDTO optimisticDTO = new OptimisticDTO();

    @Test
    public void testOptimisticLocking() throws Exception {
        final String userId = UUID.randomUUID().toString();
        final Instant creationDate = Instant.now();

        final Optimistic optimistic = new Optimistic();
        optimistic.setDescripton("description-01");
        optimistic.setCreatorId(userId);
        optimistic.setModifierId(userId);
        optimistic.setCreatedAt(creationDate);
        optimistic.setLastModified(creationDate);
        optimisticRepository.saveAndFlush(optimistic);

        final ExecutorService executorService = Executors.newFixedThreadPool(2);

        final Callable<Boolean> task1 = () -> {
            try {
                optimisticDTO.setDescripton("Desc: Updated by thread 1");
                optimisticService.updateOptimistic(optimistic.getId(), optimisticDTO);
                return true;
            } catch (Exception e) {
                System.out.println("Thread 1 failed due to: " + e.getMessage());
                return false;
            }
        };

        final Callable<Boolean> task2 = () -> {
            try {
                optimisticDTO.setDescripton("Desc: Updated by thread 2");
                optimisticService.updateOptimistic(optimistic.getId(), optimisticDTO);
                return true;
            } catch (Exception e) {
                System.out.println("Thread 2 failed due to: " + e.getMessage());
                return false;
            }
        };

        final Future<Boolean> result1 = executorService.submit(task1);
        final Future<Boolean> result2 = executorService.submit(task2);


        final boolean success1 = result1.get();
        final boolean success2 = result2.get();

        Assertions.assertTrue(success1 != success2, "One of the updates should have failed due to optimistic locking.");

        executorService.shutdown();
    }
}
