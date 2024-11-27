# 1단계: 빌드 단계
FROM eclipse-temurin:17-jdk-jammy as builder
WORKDIR /app

# Gradle Wrapper 실행 권한 부여
COPY gradlew .
RUN chmod +x gradlew

# Gradle Wrapper와 소스 복사
COPY gradle/ gradle/
COPY build.gradle.kts settings.gradle.kts ./
COPY api/ api/
COPY service/ service/
COPY common/ common/
COPY client/ client/
COPY domain/ domain/
COPY mock/ mock/

# Gradle 빌드 실행
RUN ./gradlew :api:bootJar --no-daemon

# 2단계: 실행 단계
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=builder /app/api/build/libs/*.jar app.jar

# 실행 명령어
CMD ["java", "-jar", "app.jar"]