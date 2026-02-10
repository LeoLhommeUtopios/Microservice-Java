#!/bin/bash
set -e

# Fonction pour créer une base de données si elle n'existe pas
create_database() {
    local database=$1
    echo "Création de la base de données: $database"
    
    mysql -u root -p"$MYSQL_ROOT_PASSWORD" <<-EOSQL
        CREATE DATABASE IF NOT EXISTS \`$database\`;
EOSQL
    
    echo "Base de données $database créée (ou existante)"
}

# Créer les bases de données pour chaque microservice
create_database "users"
create_database "orders"


echo "Initialisation des bases de données terminée "