#build stage
FROM maven:3.6.3-adoptopenjdk-14 as build

WORKDIR /tmp/
COPY pom.xml /tmp/pom.xml
COPY src /tmp/src

RUN mvn package -DskipTests

#run stage
FROM debezium/connect
ENV DEBEZIUM_DIR=$KAFKA_CONNECT_PLUGINS_DIR/debezium-transformer

RUN mkdir $DEBEZIUM_DIR
COPY --from=build /tmp/target/*.jar $DEBEZIUM_DIR