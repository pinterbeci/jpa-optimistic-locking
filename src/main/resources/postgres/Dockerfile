FROM postgres:16.1

ENV POSTGRES_USERNAME=postgres
ENV POSTGRES_PASSWORD=postgres
ENV PGDATA=/var/lib/postgresql/data/pgdata

COPY initdb/1_init_database.sql /docker-entrypoint-initdb.d
COPY initdb/2_create-extensions.sh /docker-entrypoint-initdb.d