# Use the official OpenJDK 21 image as the base image
FROM openjdk:21-jdk-slim AS build

# Set the working directory
WORKDIR /app

# Copy the Gradle wrapper and build files
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY gradle/wrapper/ gradle/wrapper/

# Copy the source code
COPY src ./src

# Build the project
RUN ./gradlew build --no-daemon

# Create a new stage for the runtime environment
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port on which the application will run
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
