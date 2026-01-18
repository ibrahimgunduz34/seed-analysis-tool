ARG MAVEN_VERSION=3.9-eclipse-temurin-21
ARG JRE_VERSION=21-jre

FROM maven:${MAVEN_VERSION} AS build

ARG CI_COMMIT_REF_NAME=dev

WORKDIR /build
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
COPY docker-entrypoint.sh ./docker-entrypoint.sh

RUN chmod a+x ./docker-entrypoint.sh
RUN mvn -B -q package -DskipTests -Drevision=${CI_COMMIT_REF_NAME}

FROM eclipse-temurin:${JRE_VERSION}
ENV JAVA_OPTS="-Xms256m -Xmx1g"

WORKDIR /app
COPY --from=build /build/target/seed-analysis-1.0-SNAPSHOT.jar seed-analysis.jar
COPY --from=build /build/docker-entrypoint.sh docker-entrypoint.sh
EXPOSE 8080

ENTRYPOINT ["/app/docker-entrypoint.sh"]

