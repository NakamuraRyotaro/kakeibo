# ビルドステージ
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle
RUN ./gradlew dependencies --no-daemon || true
COPY src ./src
RUN ./gradlew bootJar --no-daemon

# 実行ステージ
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]