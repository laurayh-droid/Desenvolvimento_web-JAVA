FROM eclipse-temurin:17-jre-jammy

ARG JAR_FILE=target/gateway-service-1.0.0.jar
WORKDIR /app
COPY ${JAR_FILE} app.jar
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
ENTRYPOINT ["java", "-jar", "app.jar"]

HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
	CMD curl -f http://localhost:8080/actuator/health || exit 1
