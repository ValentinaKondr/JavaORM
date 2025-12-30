# Платформа онлайн-обучения

Данный проект представляет собой учебную платформу для онлайн-курсов, посвящённых работе с ORM и Hibernate. Решение демонстрирует практическое использование **Spring Boot**, **JPA/Hibernate** и **PostgreSQL** при разработке комплексной системы управления образовательным процессом.

---

## Функциональность

- **Управление пользователями**: студенты, преподаватели и администраторы  
- **Каталог курсов**: создание, редактирование и просмотр курсов  
- **Структура курсов**: модули, уроки, задания  
- **Система тестирования**: викторины с вопросами и вариантами ответов  
- **Запись на курсы**: связи Many-to-Many между студентами и курсами  
- **Система оценивания**: проверка заданий и выставление баллов  
- **Отзывы и рейтинги**: обратная связь от студентов  

---

## Архитектура

Проект реализован с использованием многослойной архитектуры:

- **Entity** — JPA-сущности и связи между ними  
- **Repository** — Spring Data JPA репозитории  
- **Service** — бизнес-логика и управление транзакциями  
- **Controller** — REST API эндпойнты  
- **Config** — конфигурация приложения  

---

## Технологии

- **Java 17**
- **Spring Boot 3.2.x**
- **Spring Data JPA**
- **Hibernate**
- **PostgreSQL**
- **Testcontainers**
- **Gradle**

---

## Модель данных

В проекте реализовано более **17 JPA-сущностей** с различными типами связей.

### Основные сущности

- **User** — пользователь системы  
- **Profile** — профиль пользователя (One-to-One)  
- **Category** — категория курсов  
- **Course** — учебный курс  
- **Enrollment** — запись студента на курс  
- **Module** — модуль курса  
- **Lesson** — урок  
- **Assignment** — задание  
- **Submission** — решение задания  
- **Quiz** — тест  
- **Question** — вопрос теста  
- **AnswerOption** — вариант ответа  
- **QuizSubmission** — результат прохождения теста  
- **CourseReview** — отзыв о курсе  
- **Tag** — теги курсов  

Все связи настроены с **FetchType.LAZY** для демонстрации особенностей ORM.

---

## Установка и запуск

### Требования

1. **Java 17** или выше  
2. **Gradle** (или Gradle Wrapper)

---

### Быстрый запуск

Проект поддерживает **два режима работы с базой данных**.

#### Вариант 1: H2 (embedded)

```bash
./gradlew bootRun --args='--spring.profiles.active=h2'
```

#### Вариант 2: PostgreSQL

**Через Docker Compose (рекомендуется):**
```bash
docker-compose up -d
```

**Ручная настройка БД:**
```sql
CREATE DATABASE education_platform;
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE education_platform TO postgres;
```

**Запуск приложения:**
```bash
./gradlew bootRun
```

Приложение доступно по адресу:  
`http://localhost:8080`

---

## H2 Console

Доступна при использовании профиля **h2**:

- URL: `http://localhost:8080/h2-console`  
- JDBC URL: `jdbc:h2:mem:testdb`  
- Username: `sa`  
- Password: *(пусто)*  

---

## REST API

### Курсы

```http
POST /api/courses
GET /api/courses/{id}
GET /api/courses
GET /api/courses/category/{categoryId}
GET /api/courses/teacher/{teacherId}
POST /api/courses/{courseId}/modules
```

---

### Запись на курсы

```http
POST /api/enrollments
DELETE /api/enrollments
GET /api/enrollments/student/{studentId}/courses
```

---

### Задания

```http
POST /api/assignments
POST /api/assignments/{assignmentId}/submit
PUT /api/assignments/submissions/{submissionId}/grade
```

---

### Тестирование

```http
POST /api/quizzes
POST /api/quizzes/{quizId}/questions
POST /api/quizzes/questions/{questionId}/options
POST /api/quizzes/{quizId}/take
```

---

## Демо-данные

При старте приложения автоматически создаются:

- преподаватель и два студента  
- категории курсов  
- курсы с модулями и уроками  
- записи студентов  
- задания и тесты  
- отзывы  

---

## Тестирование

### Unit-тесты

- сервисы курсов  
- записи на курсы  
- задания  

### Интеграционные тесты

Используется **Testcontainers**:
- проверка репозиториев  
- end-to-end сценарии  
- работа с ленивой загрузкой  
- миграция схемы базы данных  

---

## Структура проекта

```
src/main/java/valyush/educationplatform/
├── entity/
├── repository/
├── service/
├── controller/
└── config/

src/test/java/valyush/educationplatform/
└── EducationPlatformIntegrationTest.java
```

---

## Docker и CI/CD

Проект содержит `Dockerfile` и `docker-compose.yml`.

```bash
docker-compose up -d
docker-compose down
```

Настроен CI/CD пайплайн (GitHub Actions) для сборки и запуска тестов.

---

## Итог

Проект представляет собой полноценный учебный пример backend-приложения на Spring Boot с богатой доменной моделью, REST API, тестированием и поддержкой контейнеризации.
