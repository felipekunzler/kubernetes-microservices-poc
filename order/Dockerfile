FROM maven:3-openjdk-16-slim as build

WORKDIR /app
COPY . .
RUN mvn clean package -Dmaven.test.skip=true

FROM adoptopenjdk:16-jre

RUN mkdir /app
COPY --from=build /app/target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
