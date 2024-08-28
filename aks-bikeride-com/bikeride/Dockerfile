# Use the official OpenJDK 21 image as a base for the build stage
FROM openjdk:21-jdk-slim AS builder

# Set the working directory
WORKDIR /app

# Copy Gradle build files and wrapper
COPY build.gradle settings.gradle ./
COPY gradle/ gradle/

# Copy the source code
COPY src/ src/

# Run the Gradle build
RUN ./gradlew build -x test

# Use a smaller image for the runtime
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=builder /app/build/libs/demo-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your application will run on
EXPOSE 8080

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
