FROM amazoncorretto:17-alpine-jdk
LABEL maintainer="jeongin <jeonginflow@gmail.com>"

#VOLUME /tmp

WORKDIR /app

COPY ./build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]