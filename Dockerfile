FROM  maven:3.6.3-adoptopenjdk-15 as build-stage

COPY pom.xml /build/
COPY src /build/src

WORKDIR /build

RUN mvn install -DskipTests

FROM adoptopenjdk/openjdk15

WORKDIR /export

COPY --from=build-stage /build/target/*.jar /trends2csv.jar

ENTRYPOINT ["java", "-jar", "/trends2csv.jar"]
