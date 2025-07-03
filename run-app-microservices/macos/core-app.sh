#!/usr/bin/env bash
set -euo pipefail

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

for svc in "${services[@]}"; do
  osascript <<EOF
    tell application "Terminal"
      activate
      set newTab to do script "cd '$PROJECT_ROOT/$svc' && ./mvnw spring-boot:run"
      tell newTab
        set custom title to "$svc"
      end tell
    end tell
EOF
done