# Build stage
FROM maven:3.9.5-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jdk-alpine

# Create user to run app
RUN addgroup -S app && adduser -S app -G app

# Set the working directory
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Change ownership of the jar file to the app user
RUN chown app:app app.jar

# Use user "app"
USER app

# Run the Jar file
ENTRYPOINT ["java","-jar","app.jar"]