package hu.udinfopark.optimistic.lock;

import hu.udinfopark.jpa.optimistic.test.JpaOptimisticLockingApplication;
import hu.udinfopark.jpa.optimistic.test.repository.OptimisticRepository;
import hu.udinfopark.jpa.optimistic.test.repository.entity.Optimistic;
import hu.udinfopark.jpa.optimistic.test.service.OptimisticService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SpringBootTest(classes = JpaOptimisticLockingApplication.class)
class JpaOptimisticLockingTest {

    @Autowired
    private OptimisticService optimisticService;

    @Autowired
    private OptimisticRepository optimisticRepository;

    @Test
    void contextLoads() {
    }

    @Test
    public void testOptimisticLocking() throws Exception {
        final String userId = UUID.randomUUID().toString();
        final Instant creationDate = Instant.now();

        final Optimistic optimistic = new Optimistic();
        optimistic.setDescripton("description-01");
        optimistic.setVersion(1L);
        optimistic.setCreatorId(userId);
        optimistic.setModifierId(userId);
        optimistic.setCreatedAt(creationDate);
        optimistic.setLastModified(creationDate);
        optimisticRepository.saveAndFlush(optimistic);

        final ExecutorService executorService = Executors.newFixedThreadPool(2);

        final Callable<Boolean> task1 = () -> {
            try {
                optimisticService.updateEntity(optimistic.getId(), "Desc: Updated by thread 1");
                return true;
            } catch (Exception e) {
                System.out.println("Thread 1 failed due to: " + e.getMessage());
                return false;
            }
        };

        final Callable<Boolean> task2 = () -> {
            try {
                optimisticService.updateEntity(optimistic.getId(), "Desc: Updated by thread 2");
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
