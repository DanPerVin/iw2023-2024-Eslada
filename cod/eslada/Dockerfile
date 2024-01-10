FROM alpine:latest
RUN apk update
RUN apk upgrade
RUN apk add openjdk17
COPY target /eslada
WORKDIR /eslada
#RUN java -jar eslada-1.0-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "eslada-1.0-SNAPSHOT.jar"]
