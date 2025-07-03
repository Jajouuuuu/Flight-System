#!/usr/bin/env bash
set -euo pipefail

# adjust if your Kafka lives elsewhere
KAFKA_HOME="$HOME/Downloads/kafka_2.13-3.9.1"
# Project root is two levels up from this script (../../)
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"

# List of your Spring-Boot modules
services=(
   accounting-service
    booking-service
  check-in-service
  customer-profile-service
  customer-service
  flight-service
  inventory-service
  payment-service
  reservation-service
  revenue-service
    search-service
)

# 2) One new window per service
for svc in "${services[@]}"; do
  gnome-terminal \
    --title="$svc" -- bash -ic \
      "cd $PROJECT_ROOT/$svc && mvn spring-boot:run; exec bash"
done
