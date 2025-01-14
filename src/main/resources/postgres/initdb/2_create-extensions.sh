#!/bin/bash

psql -v ON_ERROR_STOP=1 --username postgres -d optimistic_db  <<-EOSQL
     create extension if not exists "uuid-ossp";
EOSQL

psql -v ON_ERROR_STOP=1 --username postgres -d optimistic_db  <<-EOSQL
     create extension if not exists "unaccent";
EOSQL

