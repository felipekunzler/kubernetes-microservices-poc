FROM maven:3.9.8-eclipse-temurin-21 as build

WORKDIR /app
COPY . .
RUN mvn clean package -Dmaven.test.skip=true

FROM eclipse-temurin:21

RUN mkdir /app
COPY --from=build /app/target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
