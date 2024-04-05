FROM adoptopenjdk/openjdk11:jdk-11.0.10_9-alpine-slim

# RUN mkdir -p /app/
# RUN apk add --update ttf-dejavu
COPY . /usr/app
WORKDIR /usr/app
# RUN chmod +x mvnw && ./gradlew clean
COPY build/libs/itaap-poc-ms-utility.jar /app/application.jar

ENTRYPOINT java \
    -XX:+UseG1GC \
    -XX:+UseStringDeduplication \
    -XX:MaxRAMPercentage=50 \
    -XX:MinRAMPercentage=20 \
    -XX:NativeMemoryTracking=summary \
    -Djava.security.egd=file:/dev/./urandom \
    $JAVA_OPTS \
    -jar /app/application.jar
