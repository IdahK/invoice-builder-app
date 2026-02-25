# Build stage
FROM eclipse-temurin:21-jdk AS builder

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for better layer caching)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Make Maven wrapper executable
RUN chmod +x ./mvnw

# Download dependencies (will be cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

# Install curl for health check
RUN apk add --no-cache curl

# Set working directory
WORKDIR /app

# Copy the built jar from builder stage
COPY --from=builder /app/target/invoicebuilder-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
CMD ["java", "-jar", "app.jar"]
