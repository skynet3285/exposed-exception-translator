# Exposed Exception Translator with Spring Boot

`exposed-exception-translator`는 JetBrains Exposed프레임워크를 Spring Boot 환경에서 사용할 때, 데이터베이스 예외를 Spring의 표준 예외 계층인
DataAccessException으로 자동 변환해 주는 라이브러리입니다.

jdbc를 기반으로 구현되었으며, r2dbc는 테스트하지 않았습니다.

## 🚀 왜 필요한가요?

1. Exposed의 예외 구조: Exposed는 내부적으로 `ExposedSQLException`을 발생시키며, 이는 Checked Exception인 `SQLException`을 감싸고 있습니다.

2. Spring JPA 변환기와의 차이
    - JPA 기반 기술을 사용할 때는 Spring이 제공하는 `PersistenceExceptionTranslationPostProcessor`을 빈으로 등록하면 됩니다.
    - 하지만 이 프로세서는 `RuntimeException`을 상속한 `PersistenceException` 계열만을 감지하여 변환하도록 설계되어 있습니다.
    - 반면 Exposed는 `SQLException`을 상속한 `ExposedSQLException`을 던지기 때문에, 기존 JPA 방식의 예외 변환기로는 Exposed의 예외를 추상화된
      `DataAccessException`으로 변환할 수 없습니다.

3. 예외 추상화의 부재: 이 라이브러리가 없다면 서비스 코드에서 특정 DB 엔진에 종속된 에러 코드를 직접 처리해야 하거나, 불필요한 try-catch (SQLException) 블록이 코드에 침투하게 됩니다.

## ✨ 장점

- 표준 예외 추상화: @Repository 스테레오타입 어노테이션이 붙은 빈에서 발생하는 예외를 DuplicateKeyException, DataIntegrityViolationException 등 Spring
  표준 예외로 변환합니다.

- 체크 예외 제거: 서비스 레이어에서 더 이상 ExposedSQLException(SQLException)을 직접 다룰 필요가 없습니다.

- DB 독립성: 어떤 데이터베이스(H2, MySQL, PostgreSQL 등)를 사용하든 동일한 Spring 표준 예외 인터페이스로 대응할 수 있습니다.

## 🛠 설정 방법 (Configuration)

`ExposedExceptionTranslatorConfig`을 확인하면 됩니다.
Bean 주입으로 설정할 수 있습니다

상세한 동작 확인과 테스트 코드는 [example 디렉토리](./src/test/kotlin/com/github/skynet3285/exposed/exception/translator/example)를 참고해
주세요. H2 메모리 DB 환경에서의 실제 예외 변환 테스트 케이스가 포함되어 있습니다.
