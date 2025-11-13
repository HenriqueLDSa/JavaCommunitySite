# ---- Build Stage ----
FROM maven:3.9.9 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Generate jOOQ classes
RUN mvn generate-sources

# Package application
RUN mvn clean package -DskipTests

# ---- Runtime Stage ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
