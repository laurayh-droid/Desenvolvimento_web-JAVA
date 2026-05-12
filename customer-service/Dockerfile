FROM eclipse-temurin:17-jre
WORKDIR /app
ARG JAR_FILE=target/customer-service-1.0.0.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
