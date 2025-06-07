#!/usr/bin/env bash
set -euo pipefail

# adjust if your Kafka lives elsewhere
KAFKA_HOME="$HOME/Downloads/kafka_2.13-3.9.1"
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# List of your Spring-Boot modules
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
#search-service
)

# 1) (Optional) Start ZK & Kafka tabs in one window
gnome-terminal \
  --title="ZooKeeper" -- bash -ic \
    "cd $KAFKA_HOME && bin/zookeeper-server-start.sh config/zookeeper.properties; exec bash" \


gnome-terminal \
  --title="Kafka" -- bash -ic \
    "cd $KAFKA_HOME && bin/kafka-server-start.sh config/server.properties; exec bash"

# give them a moment
sleep 8

# 2) One new window per service
for svc in "${services[@]}"; do
  gnome-terminal \
    --title="$svc" -- bash -ic \
      "cd $PROJECT_ROOT/$svc && mvn spring-boot:run; exec bash"
done
