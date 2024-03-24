FROM maven:3.9.6-amazoncorretto-21 AS builder
COPY ./ ./
RUN mvn clean package -DskipTests
FROM openjdk:21-jdk
COPY --from=builder  /target/App-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]
