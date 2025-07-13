# EC2 배포 가이드

## 사전 준비사항

1. **EC2 인스턴스 접속**

   ```bash
   ssh -i your-key.pem ec2-user@43.200.100.172
   ```

2. **보안 그룹 설정**
   - 인바운드 규칙에 8080 포트 추가
   - 소스: 0.0.0.0/0 (모든 IP 허용) 또는 특정 IP

## 배포 단계

### 1. 로컬에서 JAR 파일 빌드

```bash
./gradlew clean build
```

### 2. JAR 파일을 EC2로 전송

```bash
scp -i your-key.pem build/libs/pda-midterm-project-0.0.1-SNAPSHOT.jar ec2-user@43.200.100.172:/home/ec2-user/pda-app/
```

### 3. EC2에서 배포 스크립트 실행

```bash
# EC2에 접속 후
chmod +x deploy.sh
./deploy.sh
```

### 4. 애플리케이션 상태 확인

```bash
# 프로세스 확인
ps aux | grep pda-midterm-project

# 로그 확인
tail -f /home/ec2-user/pda-app/app.log

# 포트 확인
netstat -tlnp | grep 8080
```

### 5. 애플리케이션 접속 테스트

브라우저에서 `http://43.200.100.172:8080` 접속

## 문제 해결

### Java 설치 문제

```bash
# Amazon Linux 2
sudo yum update -y
sudo yum install -y java-17-amazon-corretto

# 버전 확인
java -version
```

### 포트 문제

```bash
# 방화벽 확인
sudo firewall-cmd --list-all

# 포트 열기 (필요시)
sudo firewall-cmd --permanent --add-port=8080/tcp
sudo firewall-cmd --reload
```

### 애플리케이션 재시작

```bash
# 프로세스 종료
pkill -f pda-midterm-project

# 재시작
cd /home/ec2-user/pda-app
nohup java -jar pda-midterm-project-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod > app.log 2>&1 &
```

## 로그 위치

- 애플리케이션 로그: `/home/ec2-user/pda-app/app.log`
- 데이터베이스 파일: `/home/ec2-user/pda-app/data/`
