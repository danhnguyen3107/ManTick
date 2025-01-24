FROM openjdk:23


# Set the working directory
WORKDIR /app

# Copy the Spring Boot JAR file into the container
COPY target/ManTick-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]