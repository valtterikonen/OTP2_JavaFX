# Use the official Maven image to build the application
FROM maven:3.8.6-openjdk-11 AS builder

# Set the working directory
WORKDIR /app

# Copy the pom.xml and install dependencies
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Use a smaller base image for the final runtime
FROM openjdk:11-jre-slim

# Set the working directory in the runtime image
WORKDIR /app

# Copy the built jar file from the builder image
COPY --from=builder /app/target/*.jar app.jar

# Specify the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

