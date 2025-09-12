#FROM maven:3-openjdk-16-slim as baseImage
#
#WORKDIR /opt/confirmation
#COPY pom.xml /opt/confirmation
#
#RUN mvn dependency:resolve-plugins dependency:resolve dependency:go-offline




FROM paymentbaseimage as build

WORKDIR /opt/payment
COPY pom.xml /opt/payment
COPY . .
RUN mvn package -Dmaven.test.skip=true


FROM adoptopenjdk:16-jre-openj9 as deploy

LABEL maintainer="Paulo Gräbin <paulograbin@gmail.com>"
LABEL org.label-schema.name="Payment"

EXPOSE 3000

ENV confirmation_mysql_connection=jdbc:mysql://db:3306/payment?createDatabaseIfNotExist=true
ENV confirmation_mysql_username=root
ENV confirmation_mysql_password=root

WORKDIR /opt/payment/
COPY --from=build /opt/payment/target/Payment-1.0.jar .
ENTRYPOINT ["java", "-jar", "/opt/payment/Payment-1.0.jar"]
