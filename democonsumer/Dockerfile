#build stage
FROM maven:3.6.3-adoptopenjdk-14 as build

WORKDIR /tmp/
COPY pom.xml /tmp/pom.xml
COPY src /tmp/src

RUN mvn package -DskipTests

#run stage
FROM adoptopenjdk:14-jdk-hotspot

COPY --from=build /tmp/target/*.jar app.jar

COPY ./wait-for-it.sh /wait-for-it.sh

CMD ["./wait-for-it.sh", "demo-db:5432", "--", "java", "-jar", "/app.jar"]