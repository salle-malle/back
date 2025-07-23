FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# JAR 파일 복사
COPY build/libs/*.jar app.jar

# data 폴더와 그 안의 CSV 파일 복사
COPY data/ data/

# 실행 권한 부여
RUN chmod +x app.jar

CMD ["java", "-jar", "app.jar"]
