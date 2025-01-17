package hu.udinfopark.optimistic.lock;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {
    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgreSQLContainer() {
        final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:16.1")
            .withDatabaseName("optimistic_test_db")
            .withUsername("test")
            .withPassword("test")
            .withCopyFileToContainer(
                MountableFile.forClasspathResource(
                    "1_test_create-extensions.sh"), "/docker-entrypoint-initdb.d/"
            )
            .withCopyFileToContainer(
                MountableFile.forClasspathResource(
                    "db-scripts/2_create_test_optimistic_table.sql"), "/docker-entrypoint-initdb.d/"
            );
        postgresqlContainer.start();
        return postgresqlContainer;
    }

    @Bean
    @ServiceConnection
    public DataSource dataSource(@Autowired final PostgreSQLContainer<?> postgreSQLContainer) {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(postgreSQLContainer.getJdbcUrl());
        dataSource.setUsername(postgreSQLContainer.getUsername());
        dataSource.setPassword(postgreSQLContainer.getPassword());
        return dataSource;
    }
}