<<<<<<< HEAD
# Stage 1: Build the application with JDK 17
FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests -X

# Stage 2: Create a smaller runtime image
FROM openjdk:17

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

=======
# Stage 1: Build the application
FROM maven:3.8.4-openjdk-11 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and source files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create a smaller runtime image
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy only the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Command to run the application
>>>>>>> 25ab223b68c14da3788304d6281e1364e45143de
ENTRYPOINT ["java", "-jar", "app.jar"]
