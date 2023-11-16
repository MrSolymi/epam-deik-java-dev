FROM amazoncorretto:21
ENV APP_HOME="/app"

COPY ticket-service/target/ticket-service-0.0.1-SNAPSHOT.jar ${APP_HOME}/ticket-service-0.0.1-SNAPSHOT.jar
WORKDIR ${APP_HOME}
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "ticket-service-0.0.1-SNAPSHOT.jar"]