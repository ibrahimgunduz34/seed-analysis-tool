FROM openjdk:26-ea-21-jdk-oracle

ENV JAVA_OPTS="-Xms256m -Xmx1g"

COPY target/seed-analysis-1.0-SNAPSHOT.jar /app/seed-analysis.jar
COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod a+x /docker-entrypoint.sh

ENTRYPOINT ["/docker-entrypoint.sh"]
