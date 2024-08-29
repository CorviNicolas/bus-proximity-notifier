FROM maven:3-amazoncorretto-21-debian AS builder

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk

WORKDIR /app/

COPY --from=builder /app/bus-logger-notifier/target/bus-logger-notifier-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "bus-logger-notifier-0.0.1-SNAPSHOT.jar"]