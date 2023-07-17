# Stage 1
# Initialize build and set base image for first stage
FROM maven:3.9.3-eclipse-temurin-20-alpine AS build

# Set working directory
WORKDIR /parkinglotII/backend/

# Copy resources into container
COPY mvnw .
COPY pom.xml .

# Check and download necessary dependecies
# RUN mvn dependency:tree

# Copy code source into container
COPY src/ src/

# Use mvn package command to validate, compile, package and verify code source
## -DskipTests argument is used to skip tests and integration tests
RUN mvn -DskipTests package

# Stage 2
# Run built app from stage 1
FROM eclipse-temurin:20-jre-alpine AS run

# Set deployment directory
WORKDIR /parkinglotII/backend/release/

# Declare an argument JAVA_NAME
# it can be used to specifi a custom name in ENTRYPOINT command when start the container
ARG WAR_NAME

# Copy War file which was built in stage 1 and give a name
COPY --from=build /parkinglotII/backend/target/*.war app.war

# Informative directive
# This app will run on port 8080
EXPOSE 8080

# ENTRYPOINT command, it is used to give arguments and parameters from command line when start the container
ENTRYPOINT ["java", "-jar", "app.war"]
