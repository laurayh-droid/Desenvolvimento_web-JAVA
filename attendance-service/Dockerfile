FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
ARG JAR_FILE=target/attendance-service-1.0.0.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8085
RUN apk add --no-cache curl
ENTRYPOINT ["java", "-jar", "app.jar"]

HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
	CMD curl -f http://localhost:8085/actuator/health || exit 1
