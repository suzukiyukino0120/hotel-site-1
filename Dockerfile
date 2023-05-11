#
# Build stage
#
FROM maven:4.0.0-jdk-11 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:11-jdk-slim
COPY --from=build /target/hotel-site-1-0.0.1-SNAPSHOT.jar hotel-site-1.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","hotel-site-1.jar"]