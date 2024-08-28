# Use an OpenJDK base image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy Gradle build files and source code
COPY build.gradle settings.gradle ./
COPY gradle/ gradle/
COPY src/ src/

# Run the Gradle build
RUN ./gradlew build

# Copy the JAR file to the image
COPY build/libs/demo-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
