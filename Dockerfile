FROM maven:3.5-jdk-8-alpine as build
COPY . /opt/build/
RUN cd /opt/build && \
    mvn package

FROM openjdk:8-alpine
ENTRYPOINT /usr/local/bin/spring-boot
RUN apk update && apk add bash
COPY --from=build /opt/build/backend/target/*.jar /usr/local/bin/spring-boot
