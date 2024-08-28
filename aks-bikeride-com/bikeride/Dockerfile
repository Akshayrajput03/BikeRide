# Use the official OpenJDK 21 image as the base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the Docker container
WORKDIR /app

# Copy the build.gradle and settings.gradle files
COPY aks-bikeride-com/bikeride/build.gradle aks-bikeride-com/bikeride/settings.gradle ./

# Copy the gradle wrapper
COPY aks-bikeride-com/bikeride/gradle/ gradle/

# Copy the source code
COPY aks-bikeride-com/bikeride/src/ src/

# Copy the JAR file built by Gradle
COPY aks-bikeride-com/bikeride/build/libs/demo-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
