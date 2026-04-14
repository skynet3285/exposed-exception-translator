# Exposed Exception Translator with Spring Boot

`exposed-exception-translator`는 JetBrains Exposed 프레임워크를 Spring Boot 환경에서 사용할 때, 데이터베이스 예외를 Spring의 표준 예외 계층인
DataAccessException으로 자동 변환해 주는 라이브러리입니다.

jdbc의 예외를 변환시켜줍니다.

## 🤖 환경

- Spring Boot 4
- Exposed 1.0.0


## 🚀 왜 필요한가요?

- Exposed는 `SQLException` 기반 예외를 사용하여 Spring의 기본 예외 변환 대상이 아닙니다.
- 그 결과 서비스 레이어에 DB 종속적인 예외 처리 코드가 침투하게 됩니다.

이 라이브러리는 Exposed의 예외를 Spring의 `DataAccessException` 계층으로 변환하여 일관된 예외 처리와 DB 독립성을 제공합니다.

## ✨ 장점

- 표준 예외 추상화: @Repository 스테레오타입 어노테이션이 붙은 빈에서 발생하는 예외를 DuplicateKeyException, DataIntegrityViolationException 등 Spring
  표준 예외로 변환합니다.

- 체크 예외 제거: 서비스 레이어에서 더 이상 ExposedSQLException(SQLException)을 직접 다룰 필요가 없습니다.

- DB 독립성: 어떤 데이터베이스(H2, MySQL, PostgreSQL 등)를 사용하든 동일한 Spring 표준 예외 인터페이스로 대응할 수 있습니다.

## 📦 설치

이 라이브러리는 JitPack을 통해 배포됩니다.

build.gradle.kts
```kotlin
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.skynet3285:exposed-exception-translator:1.0.2")
}
```

## 🛠 설정 방법 (Configuration)

`ExposedExceptionTranslatorConfig`을 확인하면 됩니다.

예를 들어 아래와 같이 사용합니다.
```kotlin
@Configuration
class ExposedExceptionTranslatorConfig {
    @Bean
    fun exposedExceptionTranslator(dataSource: DataSource) = ExposedExceptionTranslator(dataSource)

    @Bean
    fun exposedExceptionTranslationPostProcessor(): ExposedExceptionTranslationPostProcessor = ExposedExceptionTranslationPostProcessor()
}
```

상세한 동작 확인과 테스트 코드는 [example 디렉토리](./src/test/kotlin/com/github/skynet3285/exposed/exception/translator/example)를 참고해
주세요. H2 메모리 DB 환경에서의 실제 예외 변환 테스트 케이스가 포함되어 있습니다.
