FROM maven:3-eclipse-temurin-17-alpine as build
WORKDIR /app
COPY pom.xml .
COPY src ./src 
RUN mvn clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17.0.8.1_1-jdk-focal 
WORKDIR /app
COPY --from=build /app/target/springboot-thymeleaf-crud-web-app-0.0.1-SNAPSHOT.jar  ./app.jar
ENTRYPOINT [ "java","-jar","app.jar" ]
