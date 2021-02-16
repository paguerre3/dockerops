FROM adoptopenjdk/openjdk11:alpine-jre

# pre-requisite: ./gradlew bootJar already executed by the CI pipeline
ARG JAR_FILE_HOST_LOCATION=dockeropsvc/build/libs/dockeropsvc*.jar

WORKDIR /usr/app/

COPY ${JAR_FILE_HOST_LOCATION} app.jar

# ref: https://morioh.com/p/bba7ae845c56
# image build default name is docker.io/library/dockeropsvc:1.0
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]