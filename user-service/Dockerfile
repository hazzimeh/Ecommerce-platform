# Use a base image with Java 17
FROM openjdk:17-jdk-slim

# Create a volume to store the temporary files
VOLUME /tmp

# ARG for the jar file to copy
ARG JAR_FILE=target/user-service-1.0-SNAPSHOT.jar

# Copy the jar file to the container
COPY ${JAR_FILE} app.jar

# Expose the port that the application will run on
EXPOSE 8081

# Set the entry point to run the application
ENTRYPOINT ["java","-jar","/app.jar"]
