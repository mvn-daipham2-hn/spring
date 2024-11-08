# Use the official Java image as the base
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the application's jar file into the container
COPY build/libs/*.jar app.jar

# Open the port (if the application runs on port 8080)
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
