#!/bin/bash

# 배포 스크립트
echo "=== Spring Boot 애플리케이션 배포 시작 ==="

# Java 17 설치 확인
if ! command -v java &> /dev/null; then
    echo "Java가 설치되어 있지 않습니다. Java 17을 설치합니다..."
    sudo apt update
    sudo apt install -y openjdk-17-jdk
fi

# Java 버전 확인
java_version=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
echo "Java 버전: $(java -version 2>&1 | head -n 1)"

if [ "$java_version" != "17" ]; then
    echo "Java 17이 필요합니다. 현재 버전: $java_version"
    exit 1
fi

# 애플리케이션 디렉토리 생성
APP_DIR="/home/ubuntu/pda-app"
mkdir -p $APP_DIR
cd $APP_DIR

# 기존 애플리케이션 중지
if pgrep -f "pda-midterm-project" > /dev/null; then
    echo "기존 애플리케이션을 중지합니다..."
    pkill -f "pda-midterm-project"
    sleep 5
fi

# JAR 파일 실행
echo "애플리케이션을 시작합니다..."
nohup java -jar pda-midterm-project-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod > app.log 2>&1 &

echo "애플리케이션이 백그라운드에서 실행 중입니다."
echo "로그 확인: tail -f $APP_DIR/app.log"
echo "프로세스 확인: ps aux | grep pda-midterm-project"
