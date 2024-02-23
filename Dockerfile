FROM amazoncorretto:17-alpine-jdk
LABEL maintainer="jeongin <jeonginflow@gmail.com>"

WORKDIR /app

COPY ./build/libs/*.jar ./
COPY ./src/main/resources/static ./static
ENTRYPOINT ["java","-jar","/app/Market-Bridge-BE-0.0.1-SNAPSHOT.jar"]