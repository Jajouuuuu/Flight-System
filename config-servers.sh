#!/usr/bin/env bash
set -euo pipefail
set +e

# Chemin de Kafka (à adapter si nécessaire)
KAFKA_HOME="$HOME/Downloads/kafka_2.13-3.9.1"
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Liste des microservices Spring Boot à démarrer
services=(
  configuration
  discovery
  gateway
#  accounting-service
#  booking-service
#  check-in-service
#  customer-profile-service
#  customer-service
#  flight-service
#  inventory-service
#  payment-service
#  reservation-service
#  revenue-service
#  search-service
)

# 1) Lancer ZooKeeper dans un nouvel onglet
osascript -e 'tell application "Terminal"
  do script "cd '"$KAFKA_HOME"' && bin/zookeeper-server-start.sh config/zookeeper.properties"
end tell'

# 2) Lancer Kafka dans un autre onglet
osascript -e 'tell application "Terminal"
  do script "cd '"$KAFKA_HOME"' && bin/kafka-server-start.sh config/server.properties"
end tell'

# Laisser le temps à Kafka de démarrer
sleep 8

# 3) Lancer chaque microservice dans un nouvel onglet Terminal
for svc in "${services[@]}"; do
  osascript -e 'tell application "Terminal"
    do script "cd '"$PROJECT_ROOT/$svc"' && mvn spring-boot:run"
  end tell'
done
