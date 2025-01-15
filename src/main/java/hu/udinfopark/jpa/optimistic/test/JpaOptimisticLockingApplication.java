package hu.udinfopark.jpa.optimistic.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpaOptimisticLockingApplication {
    public static void main(String[] args) {
        SpringApplication.run(JpaOptimisticLockingApplication.class, args);
    }
}
