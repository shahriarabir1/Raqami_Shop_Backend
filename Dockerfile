FROM maven:3.9.7-openjdk-22 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests


FROM amazoncorretto:22


WORKDIR /app
COPY --from=build /build/target/docker-spring-boot-*.jar /app/

EXPOSE 8088

CMD ["java","-jar","docker-spring-boot.jar"]
