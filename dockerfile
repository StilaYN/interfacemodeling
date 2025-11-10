FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

# Копируем Gradle wrapper и зависимости для кэширования
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Загружаем зависимости (ускоряет последующие сборки)
RUN ./gradlew dependencies --no-daemon

# Копируем исходный код
COPY src src

# Собираем JAR
RUN ./gradlew bootJar --no-daemon

# Этап 2: запуск (минимальный образ)
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Копируем JAR из builder-стадии
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 5000

ENTRYPOINT ["java", "-jar", "app.jar"]