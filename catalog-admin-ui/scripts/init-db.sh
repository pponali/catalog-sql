#!/bin/bash
set -e

# Function to create a database if it doesn't exist
create_database() {
  local database=$1
  echo "Creating database: $database"
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE $database;
    GRANT ALL PRIVILEGES ON DATABASE $database TO $POSTGRES_USER;
EOSQL
}

# Create databases for each microservice
create_database "catalog_admin_bff"
create_database "catalog_db"
create_database "price_db"
create_database "promotion_db"
create_database "user_db"

echo "All databases created successfully!"

# Connect to each database and create schema if needed
for DB in "catalog_admin_bff" "catalog_db" "price_db" "promotion_db" "user_db"
do
  echo "Setting up schema for $DB"
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$DB" <<-EOSQL
    CREATE SCHEMA IF NOT EXISTS public;
    GRANT ALL ON SCHEMA public TO $POSTGRES_USER;
    GRANT ALL ON SCHEMA public TO public;
EOSQL
done

echo "Database initialization completed successfully!"