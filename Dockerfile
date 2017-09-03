FROM maven:3.5-jdk-8 as build
COPY backend /opt/build/backend
COPY ui /opt/build/ui
COPY pom.xml /opt/build/
RUN cd /opt/build && \
    mvn package

FROM openjdk:8-alpine
RUN apk update && apk add bash nginx

ENTRYPOINT ["/usr/local/bin/start.sh"]
ENV ENCRYPTION_KEY=""

COPY --from=build /opt/build/backend/target/*.jar /usr/local/bin/spring-boot
COPY --from=build /opt/build/ui/dist /opt/ui
COPY container/nginx.conf /etc/nginx/nginx.conf
COPY container/start.sh /usr/local/bin/