FROM maven:3.9.5-amazoncorretto-21 AS builder

WORKDIR /app

COPY pom.xml .

RUN  mvn dependency:go-offline -B

COPY src /app/src

RUN mvn package -DskipTests

FROM amazoncorretto:21-alpine-jdk

ENV PORT 8080
EXPOSE ${PORT}

COPY --from=builder /app/target/GeradorReceitas-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]