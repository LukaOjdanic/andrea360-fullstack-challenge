FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

# Build 
RUN mvn package -DskipTests

# Create the runtime image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=build /app/target/fitness-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "fitness-0.0.1-SNAPSHOT.jar"]
