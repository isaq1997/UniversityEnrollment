FROM gradle:8-jdk17 AS build
WORKDIR /home/gradle/project
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
RUN gradle dependencies --no-daemon || true
COPY src ./src
RUN gradle bootJar -x test --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app
RUN useradd -r -u 1001 appuser
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]