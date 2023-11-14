FROM openjdk:17-jdk-slim as build
ENV APP_HOME=/home/gradle/project
RUN mkdir -p $APP_HOME
WORKDIR $APP_HOME
COPY build.gradle.kts settings.gradle.kts gradlew $APP_HOME
COPY gradle $APP_HOME/gradle
RUN ./gradlew build -x check || true
COPY . $APP_HOME
RUN ./gradlew clean build -x check

FROM openjdk:17-jdk-slim
RUN mkdir -p /opt
WORKDIR /opt
COPY --from=build /home/gradle/project/build/libs/currency-rates.jar /opt/currency-rates.jar
ENTRYPOINT ["java",  "-jar", "currency-rates.jar"]
