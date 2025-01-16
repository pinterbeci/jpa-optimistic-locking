package hu.udinfopark.optimistic.lock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {
    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgreSQLContainer() {
        final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:16.1")
            .withCopyFileToContainer(
                MountableFile.forClasspathResource(
                    "1_test_create-extensions.sh"), "/docker-entrypoint-initdb.d/"
            )
            .withCopyFileToContainer(
                MountableFile.forClasspathResource(
                    "db-scripts/2_create_test_optimistic_table.sql"), "/docker-entrypoint-initdb.d/2_create_test_optimistic_table.sql"
            );
        postgresqlContainer.start();
        return postgresqlContainer;
    }
}