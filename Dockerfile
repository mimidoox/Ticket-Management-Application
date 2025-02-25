
# Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
# Click nbfs://nbhost/SystemFileSystem/Templates/Other/Dockerfile to edit this template

# Use an OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the target directory
COPY target/ticketManager-0.0.1-SNAPSHOT.jar app.jar

# Expose the port on which the Spring Boot app runs
EXPOSE 9090

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

