# ========================================
# Stage 1: Build Stage
# ========================================
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Give execute permissions to mvnw
RUN chmod +x mvnw || true

# Download dependencies (this layer will be cached)
RUN mvn dependency:go-offline -B || true

# Copy source code
COPY src ./src

# Build the application (skip tests for faster build)
RUN mvn clean package -DskipTests -B

# ========================================
# Stage 2: Runtime Stage
# ========================================
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy the JAR from builder stage (using wildcard to handle any version)
COPY --from=builder /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Set production profile as default
ENV SPRING_PROFILES_ACTIVE=prod


# Run the application
ENTRYPOINT ["java", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-jar", \
    "app.jar"]
