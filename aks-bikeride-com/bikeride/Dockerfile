# Use a base image with JDK 21
FROM openjdk:21-jdk-slim as build

# Set the working directory
WORKDIR /app

# Copy Gradle build files and the Gradle wrapper
COPY gradle/ gradle/
COPY build.gradle settings.gradle ./

# Copy the source code
COPY src/ src/

# Run Gradle to build the application
RUN ./gradlew build

# Use a new base image for the final stage
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/demo-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
