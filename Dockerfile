# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy as base
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw mvnw
COPY ./pom.xml ./pom.xml
RUN ./mvnw dependency:resolve
COPY src ./src
ARG ACCESS_KEY_ARG=notset
ARG SECRET_KEY_ARG=notset
ENV ACCESS_KEY=$ACCESS_KEY_ARG
ENV SECRET_KEY=$SECRET_KEY_ARG
COPY ./env-script.sh ./
RUN chmod +x ./env-script.sh
RUN ./env-script.sh

FROM base as test
RUN ["./mvnw", "test"]

FROM base as development
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=mysql", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"]

FROM base as build
RUN ./mvnw package


FROM eclipse-temurin:17-jre-jammy as production
EXPOSE 8080
COPY --from=build /app/target/microservice-labelsdetector-*.jar /microservice-labelsdetector.jar
COPY --from=build /app/.env /.env
CMD ["java", "-jar", "/microservice-labelsdetector.jar"]