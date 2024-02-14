FROM amazoncorretto:17-alpine3.18 as builder-jre

RUN apk add --no-cache --repository=http://dl-cdn.alpinelinux.org/alpine/edge/main/ binutils=2.41-r0

RUN $JAVA_HOME/bin/jlink \
         --module-path "$JAVA_HOME/jmods" \
         --verbose \
         --add-modules ALL-MODULE-PATH \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /jre

#VOLUME /tmp

ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]