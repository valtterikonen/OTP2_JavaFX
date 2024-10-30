FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY EasyBank/src ./src

# Build the application with the production profile
RUN mvn clean package -Pproduction

# Use an alternative Java 17 runtime as a parent image
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/EasyBank-0.0.1-SNAPSHOT.jar /app/EasyBank-0.0.1-SNAPSHOT.jar

# Run the application
CMD ["java", "-jar", "/app/EasyBank-0.0.1-SNAPSHOT.jar"]






