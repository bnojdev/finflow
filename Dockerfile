# Use OpenJDK 21 as base image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Create data directory for H2
RUN mkdir -p /app/data

# Expose port
EXPOSE 8096

# Run the application
CMD ["java", "-jar", "target/finflow-0.0.1-SNAPSHOT.jar"]
