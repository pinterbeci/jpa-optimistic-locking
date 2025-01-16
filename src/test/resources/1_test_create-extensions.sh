#!/bin/bash

psql -v ON_ERROR_STOP=1 --username test -d test  <<-EOSQL
     create extension if not exists "uuid-ossp";
EOSQL

psql -v ON_ERROR_STOP=1 --username test -d test  <<-EOSQL
     create extension if not exists "unaccent";
EOSQL

# Set variables for database connection
DB_NAME="test"
DB_USER="test"


# Now, search for any SQL files in the current directory and execute them
for sql_file in /docker-entrypoint-initdb.d/db-scripts/*table.sql; do
  if [ -f "$sql_file" ]; then
    echo "Executing $sql_file"
    psql -U "$DB_USER" -d "$DB_NAME" -f "$sql_file"
  fi
done
