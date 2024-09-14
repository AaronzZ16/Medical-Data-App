# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the project’s compiled JAR file and other resources
COPY target/medical-data-app-0.0.1-SNAPSHOT.jar /app/medical-data-app.jar

# Expose the port that the app runs on
EXPOSE 8090

# Set Spring Profile to use application-docker.properties
ENV SPRING_PROFILES_ACTIVE=docker

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/medical-data-app.jar"]
