# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Install X11 libraries for Swing GUI
RUN apk add --no-cache \
    fontconfig \
    ttf-dejavu \
    libx11 \
    libxext \
    libxrender \
    libxtst \
    libxi

# Copy the built JAR with dependencies from builder stage
COPY --from=builder /app/target/invoice-management-system-1.0.0-jar-with-dependencies.jar app.jar

# Set display for X11 forwarding
ENV DISPLAY=:0

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
