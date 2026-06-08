FROM eclipse-temurin:17-jre-jammy

ARG JAR_FILE=target/gateway-service-1.0.0.jar
WORKDIR /app
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
