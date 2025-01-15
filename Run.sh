#!/bin/bash

# Stop and remove existing containers
docker-compose down

# Remove the old Docker volume
docker volume rm optimistic_postgres_data

# Create a new Docker volume
docker volume create --name optimistic_postgres_data -d local

# Remove unused Docker images
docker image prune -a -f

# Navigate to the directory containing the Dockerfile
cd src/main/resources/postgres

# Build the Docker image
docker build -t udinfopark/optimistic-postgres:latest-dev .

# Return to the root directory
cd ../../../../

# Start the optimistic-postgres container in detached mode
docker-compose up -d optimistic-postgres

# Wait a few seconds to ensure the container is fully started
sleep 10

# Execute the SQL script to initialize the database
docker exec -i optimistic-postgres psql -U postgres -d optimistic_db < ./src/main/resources/postgres/init-table/create_optimistic_table.sql
