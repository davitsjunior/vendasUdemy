FROM maven:3.8.4-openjdk-11 as build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:11 as run
WORKDIR /app
COPY --from=build ./build/target/*.jar ./application.jar
EXPOSE 8080

ENTRYPOINT java -jar application.jar