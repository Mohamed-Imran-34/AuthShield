# ===== Build Stage =====
FROM maven:3.8.6-openjdk-11-slim AS build

# Set working directory
WORKDIR /app

# Copy all project files
COPY . .

# Make Maven wrapper executable (if using mvnw)
RUN chmod +x mvnw

# Build the project and skip tests
RUN mvn clean package -DskipTests

# ===== Run Stage =====
FROM openjdk:11-jre-slim

# Set working directory
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
