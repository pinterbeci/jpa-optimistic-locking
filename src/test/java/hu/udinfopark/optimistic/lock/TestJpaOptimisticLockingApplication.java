package hu.udinfopark.optimistic.lock;

import org.springframework.boot.SpringApplication;

import hu.udinfopark.jpa.optimistic.JpaOptimisticLockingApplication;

public class TestJpaOptimisticLockingApplication {
    public static void main(String[] args) {
        SpringApplication.from(JpaOptimisticLockingApplication::main)
            .with(TestcontainersConfiguration.class)
            .run(args);

    }
}
