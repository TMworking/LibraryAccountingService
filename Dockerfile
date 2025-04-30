# Stage 1 - build
FROM maven:3.9.5-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -Dcheckstyle.skip=true

# Stage 2 - final
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/src/main/resources /app/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]