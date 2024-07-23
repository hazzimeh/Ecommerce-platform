#!/bin/bash
set -e
psql -v ON_ERROR_STOP=1 --username "postgres" <<-EOSQL
  CREATE DATABASE user_service_db;
  CREATE DATABASE product_service_db;
  CREATE DATABASE order_service_db;
  CREATE DATABASE payment_service_db;
EOSQL
