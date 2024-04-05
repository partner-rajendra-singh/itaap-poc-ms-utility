FROM adoptopenjdk/openjdk11:jdk-11.0.10_9-alpine-slim

# RUN mkdir -p /app/
# RUN apk add --update ttf-dejavu
WORKDIR /app
COPY build/libs/itaap-poc-ms-utility-1.0.0.jar /app/application.jar

ENTRYPOINT java \
    -XX:+UseG1GC \
    -XX:+UseStringDeduplication \
    -XX:MaxRAMPercentage=50 \
    -XX:MinRAMPercentage=20 \
    -XX:NativeMemoryTracking=summary \
    -Djava.security.egd=file:/dev/./urandom \
    $JAVA_OPTS \
    -jar /app/application.jar
