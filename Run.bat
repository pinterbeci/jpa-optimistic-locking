@echo off

echo Removing existing Docker volume...
docker volume rm optimistic_postgres_data

echo Creating new Docker volume...
docker volume create --name optimistic_postgres_data -d local

echo Stopping Docker Compose services...
docker-compose down

echo Pruning unused Docker images...
docker image prune -a -f

echo Starting Docker Compose services...
docker-compose up -d

echo Waiting for PostgreSQL to be ready...
:waitloop
timeout /t 5 >nul
docker exec optimistic-postgres pg_isready -U postgres
if %errorlevel% neq 0 (
    echo PostgreSQL is not ready yet. Waiting...
    goto waitloop
)

echo Executing SQL script...
docker exec -i optimistic-postgres psql -U postgres -d optimistic_db < ./src/main/resources/postgres/init-table/create_optimistic_table.sql

echo SQL script executed successfully.
pause
