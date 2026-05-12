#!/bin/bash
set -e

export JAVA_HOME=$(/usr/libexec/java_home -v 17)
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

echo "=== Building Order System ==="
echo "Using Java: $JAVA_HOME"

# Limpa falhas cacheadas no .m2 para artefatos internos do projeto
find ~/.m2/repository/com/imepac -name "*.lastUpdated" -delete 2>/dev/null || true

echo ">>> Building all modules (parent + commons + services)..."
(cd "$SCRIPT_DIR" && mvn clean install -DskipTests)

echo ">>> Starting Docker Compose..."
(cd "$SCRIPT_DIR" && docker-compose up --build -d)

echo ""
echo "=== Services are up! ==="
echo "customer-service : http://localhost:8081"
echo "  Swagger UI     : http://localhost:8081/swagger-ui.html"
echo "order-service    : http://localhost:8082"
echo "  Swagger UI     : http://localhost:8082/swagger-ui.html"
