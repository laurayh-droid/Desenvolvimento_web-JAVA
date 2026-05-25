FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
ARG JAR_FILE=target/attendance-service-1.0.0.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]
