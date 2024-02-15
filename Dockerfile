FROM amazoncorretto:17-alpine-jdk
LABEL maintainer="jeongin <jeonginflow@gmail.com>"

VOLUME /tmp

COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]