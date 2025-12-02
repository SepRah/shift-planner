# Use a Java 25 runtime base image
FROM eclipse-temurin:25-jdk-jammy

# Set working directory inside container
WORKDIR /backend

# Copy the built jar from the host (after you ran mvn/gradle build)
COPY target/backend-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]