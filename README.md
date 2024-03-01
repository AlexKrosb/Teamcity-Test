# TeamCity - Test Framework with Automated Quality Control System

## Overview
The TeamCity test framework is a comprehensive solution for automating API and UI testing. Designed to ensure continuous quality control throughout the software development process. Integration with Allure and Swagger Coverage provides a detailed analysis of test results and assessment of API test coverage.

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- Any development environment supporting Lombok plugin (IntelliJ IDEA, Eclipse, VSCode)

## Running Tests
### API Test Suite
To run the API test suite, which checks the TeamCity API endpoints, execute the following command:
```sh
./mvnw test -DsuiteXmlFile=testing-suites/api-suite.xml
```
# UI Test Suite

Before running the UI test suite, ensure your environment for running UI tests (e.g., Selenoid or Selenium Grid) is correctly set up and running. Use the following command to run UI tests:

```sh
./mvnw test -DsuiteXmlFile=testing-suites/ui-suite.xml
```
# Test Execution

Tests are run through Maven:

```bash
mvn clean test
```
# Generating Reports

To generate Allure reports after running tests, use the command:

```bash
mvn allure:serve
```
Swagger Coverage reports are generated automatically and are available in the corresponding project directory.

## Used Libraries and Dependencies

The project uses the following key libraries and dependencies:

[Selenide](https://selenide.org/) for automating UI testing.

[Google Gson](https://github.com/google/gson) for working with JSON.

[Jackson](https://github.com/FasterXML/jackson) for serialization and deserialization of JSON.

[Allure Framework](https://docs.qameta.io/allure/) for generating test reports.

[RestAssured](http://rest-assured.io/) for testing REST APIs.

[Lombok](https://projectlombok.org/) to reduce boilerplate code for data models/objects.

[AssertJ](https://assertj.github.io/doc/) for enhanced checks and simplifying test writing.

[Swagger Coverage](https://github.com/viclovsky/swagger-coverage) for analyzing API coverage with Swagger documentation.
## Best Practices

- **Code Consistency**: is used Lombok annotations to keep models clean and concise.
- **API Testing**: is used RestAssured for testing API endpoints, ensuring they meet specifications.
- **UI Testing**: is used Selenide for effective and understandable user interface testing.
- **Continuous Integration**:  Integrate tests into your CI/CD pipeline for automated feedback on pull requests and commits.

___

# TeamCity - тестовый фреймворк с автоматической системой контроля качества

## Обзор
TeamCity тестовый фреймворк представляет собой комплексное решение для автоматизации тестирования API и UI. Разработан для обеспечения непрерывного контроля качества на протяжении всего процесса разработки ПО. Интеграция с Allure и Swagger Coverage обеспечивает подробный анализ результатов тестов и оценку покрытия API тестами.

## Начало работы

### Предварительные требования

- Java 17 или выше
- Maven
- Любая среда разработки, поддерживающая плагин Lombok (IntelliJ IDEA, Eclipse, VSCode)

# Запуск тестов
## Тестовый набор API
Для запуска тестового набора API, который проверяет конечные точки API TeamCity, выполните следующую команду:
```sh
./mvnw test -DsuiteXmlFile=testing-suites/api-suite.xml
```
# Тестовый набор UI

Перед запуском тестового набора UI убедитесь, что ваша среда для запуска UI тестов (например, Selenoid или Selenium Grid) настроена и запущена корректно. Для запуска тестов UI используйте следующую команду:

```sh
./mvnw test -DsuiteXmlFile=testing-suites/ui-suite.xml
```
# Запуск Тестов

Тесты запускаются через Maven:

```bash
mvn clean test
```
# Генерация Отчетов

Для генерации отчетов Allure после выполнения тестов используйте команду:

```bash
mvn allure:serve
```
Отчеты Swagger Coverage генерируются автоматически и доступны в соответствующем каталоге проекта.

## Используемые Библиотеки и Зависимости

В проекте используются следующие ключевые библиотеки и зависимости:

[Selenide](https://selenide.org/) для автоматизации UI тестирования.

[Google Gson](https://github.com/google/gson) для работы с JSON.

[Jackson](https://github.com/FasterXML/jackson) для сериализации и десериализации JSON.

[Allure Framework](https://docs.qameta.io/allure/) для генерации отчетов о тестировании.

[RestAssured](http://rest-assured.io/) для тестирования REST API.

[Lombok](https://projectlombok.org/) для уменьшения шаблонного кода для моделей/объектов данных.

[AssertJ](https://assertj.github.io/doc/) для улучшенных проверок и упрощения написания тестов.

[Swagger Coverage](https://github.com/viclovsky/swagger-coverage) для анализа покрытия API документацией Swagger.
## Лучшие практики

- **Согласованность кода**: Используется аннотации Lombok для поддержания моделей чистыми и краткими.
- **Тестирование API**: Используется RestAssured для тестирования API-эндпоинтов, убедившись, что они соответствуют спецификациям.
- **Тестирование UI**: Используется Selenide для эффективного и понятного тестирования пользовательского интерфейса.
- **Непрерывная интеграция**: Интегрируйте тесты в вашу CI/CD пайплайн для автоматизированной обратной связи по pull request'ам и коммитам.