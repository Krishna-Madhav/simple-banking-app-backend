# Dockerfile for Spring Boot Backend

# Step 1: Use an official Maven image to build the project
FROM maven:3.8.5-openjdk-18 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Step 2: Use an official JDK runtime to run the Spring Boot app
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/banking-1.0-SNAPSHOT.jar /app/backend.jar

# Expose the port for  backend
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "/app/backend.jar"]
