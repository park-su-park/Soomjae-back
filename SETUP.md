# 🛠️ 프로젝트 세팅 가이드

## ✅ 1. 프로젝트 클론

```bash
git clone https://github.com/park-su-park/Soomjae-back.git
cd Soomjae-back
```


## ☕ 2. JDK 설치
- 현재 사용 버전: JDK 17

## 🧪 3. 로컬 실행 환경
### 3.1 application.properties 직접 생성
다음 경로에 application.properties 파일을 생성합니다.
```
src/main/resources/application.properties
```
제공된 application.properties.template 파일을 복사한뒤 로컬에서 사용할 값들을 채워 주시면 됩니다.

**⚠️로컬에서 사용하는 application.properties 는 절대로 Git에 올리지 말아주세요⚠️**

## 🧼 4. 코드 스타일 (Code Style)

### 4.1 IDE 설정 (IntelliJ 기준)
1. File > Settings > Code Style > Java
2. indentation 탭 수정:
   - Tab size: 4
   - Indent: 4
   - Continuation indent: 4

## 🔍 5. Checkstyle 자동 검사
- `./gradlew build` 시 자동으로 코드 스타일 검사 실행
- 설정 위치: `build.gradle`

```groovy
checkstyle {
    toolVersion = '10.21.4' // 최신 Checkstyle 버전
    configFile = file('config/checkstyle/google_checks.xml') // Checkstyle 설정 파일 경로
}

// Checkstyle 작업을 빌드 프로세스에 추가
tasks.withType(Checkstyle).configureEach {
   reports {
      xml.required = true // XML 보고서 비활성화
      html.required = true // HTML 보고서 활성화
   }
}

// 빌드 실패를 강제화하기 위해 check 태스크에 추가
tasks.named('check').configure {
   dependsOn 'checkstyleMain', 'checkstyleTest'
}
```



