FROM amazoncorretto:17
#VOLUME /tmp

WORKDIR /app

COPY ./build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]