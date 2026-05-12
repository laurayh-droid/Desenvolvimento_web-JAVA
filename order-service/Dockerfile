FROM eclipse-temurin:17-jre
WORKDIR /app
ARG JAR_FILE=target/order-service-1.0.0.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
