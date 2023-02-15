FROM openjdk:17-jdk-alpine
COPY target/log-scheduler-0.0.1-SNAPSHOT.jar log-scheduler-1.0.0.jar
ENTRYPOINT ["java","-jar","/log-scheduler-1.0.0.jar"]
EXPOSE 8082