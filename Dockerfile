# Step 1: Use a Java base image
FROM openjdk:17-jdk-alpine

# Step 2: Set working directory
WORKDIR /app

# Step 3: Copy the built JAR into the container
COPY target/buildpro-api-gateway-0.0.1-SNAPSHOT.jar app.jar

# Step 4: Expose the port the app runs on
EXPOSE 8080

# Step 5: Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
