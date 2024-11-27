# Base image
FROM openjdk:17-jdk-slim

# JAR 파일 복사
COPY ./build/libs/mock.jar /app/mock.jar

# 컨테이너 내 작업 디렉터리 설정
WORKDIR /app

# 컨테이너가 실행될 때 JAR 파일 실행
ENTRYPOINT ["java", "-jar", "mock.jar"]

# 포트 노출 (ryan-bank: 8081, chunsik-bank: 8082)
EXPOSE 8082