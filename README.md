## 🌙 SumEarly - 밤사이 미국 증시, 한눈에

1조 · Open API 기반 금융 서비스 · **1등 수상** 🥇

### 📢 소개
AI 기술을 활용해 미국 주식 뉴스를 요약하고, 사용자의 보유 주식과 투자 성향에 맞춘 코멘트를 제공하는 개인화 투자 정보 서비스입니다. 매일 아침 최신 뉴스를 수집하고, 종목별 요약 및 성향별 인사이트를 자동 생성하여 사용자에게 실시간으로 전달합니다.


### 🕖 개발 기간
2025.07.03. ~ 2025.07.29. (1개월)


### ✅ 주요 기능
- **요약 카드**: 문단별 핵심 정리와 성향별 첨언, 하루 총평 제공
- **스크랩**: 중요한 요약 종목 정보를 저장/모아보기
- **포트폴리오**: 보유 종목의 지표 한눈에 확인
- **알림**: 매일 아침 7시 공시, 어닝콜, 요약 알림 전달


### 📱 UI/UX

| 요약 페이지 | 스크랩 페이지 | 온보딩 페이지 | 알림 페이지 |
|:---:|:---:|:---:|:---:|
| <img src="https://github.com/user-attachments/assets/b9e1e3cf-0d08-4a03-9bfe-1f17d2767f7f" width="350"/> | <img src="https://github.com/user-attachments/assets/b2ef98b0-a562-476f-977c-e02557c72b63" width="350"/> | <img src="https://github.com/user-attachments/assets/1e6cdd32-81c8-46e6-964e-227bebe4fc7e" width="200" height="450"/> | <img src="https://github.com/user-attachments/assets/5e2bc5ea-c5d1-4ac2-8327-e2de8e0c0760" width="250"/>  |

| 메인 페이지 | 마이 페이지 | 종목 상세 페이지 | 로그인 페이지 |
|:---:|:---:|:---:|:---:|
| <img src="https://github.com/user-attachments/assets/8b47ab59-26de-4f78-ab6d-ca06d29300ce" width="250"/> | <img src="https://github.com/user-attachments/assets/8db5dc70-f35a-4938-b418-e98a0efa94f1" width="250"/> | <img src="https://github.com/user-attachments/assets/6a8ba61c-cf35-48d1-ae80-c12d783a9ba3" width="250"/> | <img src="https://github.com/user-attachments/assets/ab4a619c-5f32-4e3f-ac88-5c6c9968ddbb" width="250"/> |



### 🖥️ 기술 스택
<div style="display:inline;"> 
    <img src="https://img.shields.io/badge/Next.js-000000?style=flat-square&logo=Next.js&logoColor=white"/> 
    <img src="https://img.shields.io/badge/Typescript-3178C6?style=flat-square&logo=TypeScript&logoColor=white"/> 
    <img src="https://img.shields.io/badge/TailwindCSS-06B6D4?style=flat-square&logo=tailwindcss&logoColor=white"/> 
    <img src="https://img.shields.io/badge/Figma-F24E1E?style=flat-square&logo=Figma&logoColor=white"/> 
</div>

<div style="display:inline;"> 
    <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring-Boot&logoColor=white"/> 
    <img src="https://img.shields.io/badge/FastAPI-009688?style=flat-square&logo=fastapi&logoColor=white"/> 
    <img src="https://img.shields.io/badge/MariaDB-003545?style=flat-square&logo=MariaDB&logoColor=white"/> 
</div>

<div style="display:inline;"> 
    <img src="https://img.shields.io/badge/Amazon AWS-232F3E?style=flat-square&logo=amazonaws&logoColor=white"/> 
    <img src="https://img.shields.io/badge/Gabia-0C4A60?style=flat-square&logoColor=white"/> 
    <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white"/> 
    <img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=flat-square&logo=githubactions&logoColor=white"/> 
</div>



### ⚙️ 시스템 구조도

#### 📄 공시 데이터 수집 파이프라인
<img src="https://github.com/user-attachments/assets/d33c94aa-94a0-41da-8611-3eb4d6031ca5" width="600"/>

#### 📰 종목별 뉴스 데이터 수집 파이프라인
<img src="https://github.com/user-attachments/assets/8d6ef1ec-e5f3-4c5c-8161-5bedf7292a0c" width="600"/>

#### 📊 종목별 뉴스 요약 처리 파이프라인
<img src="https://github.com/user-attachments/assets/f2b6ba70-fd1a-4162-88c8-43f0f7e797a4" width="600"/>


### 👥 팀원

| 김준호 | 소유진 | 임채연 | 황인찬 |
|:---:|:---:|:---:|:---:|
| <img src="https://github.com/user-attachments/assets/8df2c018-ade0-4553-a7d2-7dee642d677c" width="150"/> | <img src="https://github.com/user-attachments/assets/cf6e2d15-3e18-4a9b-b5c2-225cbe98776a" width="150"/> | <img src="https://github.com/user-attachments/assets/ce5180b3-6ca1-4486-8e6e-ec5dfc67fa0a" width="150"/> | <img src="https://github.com/user-attachments/assets/32835def-f80d-40f9-852e-0b2930e9e20f" width="150"/> |
| 어닝콜 캘린더<br/>종목 상세 페이지<br/>계좌 연동 API<br/>프론트 CI/CD | 알림 시스템<br/>AI 뉴스 요약 및 첨언<br/>총평 자동화<br/>마이페이지 개발 | UI/로고 디자인<br/>서버 인프라 구축<br/>공시/인증 API 및 요약<br/>메인·로그인·회원가입 | 카드/스크랩 페이지<br/>뉴스 크롤링 시스템<br/>반응형 UI 구현<br/>요약 카드 스타일링 |


