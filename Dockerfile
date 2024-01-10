# Use the official Gradle image as the base image
FROM gradle:jdk17 as builder

# Set the working directory
WORKDIR /app

# Copy the source code into the container
COPY . .

# Build the application
RUN gradle clean build

# Start a new stage with a minimal JDK 17 image
FROM openjdk:17-slim

# Set the working directory
WORKDIR /app

# Copy the jar file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the application's port
EXPOSE 8585

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]