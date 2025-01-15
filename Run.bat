@echo off

docker-compose down

docker volume rm optimistic_postgres_data

docker volume create --name optimistic_postgres_data -d local

docker image prune -a -f

cd src/main/resources/postgres

docker build -t udinfopark/optimistic-postgres:latest-dev .

cd ../../../../

docker-compose up -d optimistic-postgres

timeout /t 10

docker exec -i optimistic-postgres psql -U postgres -d optimistic_db < ./src/main/resources/postgres/init-table/create_optimistic_table.sql
