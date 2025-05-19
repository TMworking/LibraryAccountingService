# LibraryAccountingService

Сервис для учета книг в библиотеке с REST API, базой данных и аутентификацией.

## Технологии
- **Java 17** + **Spring Boot 3**
- **PostgreSQL** (хранение данных)
- **Liquibase** (миграции БД)
- **Docker** (контейнеризация)
- **Swagger** (документация API)

## Требования
- Регистрация пользователя в системе (пользователя и администратора)
- Редактирование личной информации пользователя
- Добавление, удаление, редактирование учетной единицы
- Просмотр детальной информации о единице и ее истории (запросов)
- Каталог для учетных единиц с любой глубиной вложенности подкаталогов
- Учет взятых в аренду единиц с указанием даты возврата и поиск просроченных возвратов
- Учет возвращенных единиц

## Быстрый старт
### Подготовка
- Установите [Docker](https://docs.docker.com/get-docker/) и [Docker Compose](https://docs.docker.com/compose/install/)
- Клонируйте репозиторий:
  ```bash
  git clone https://github.com/{username}/library-accounting.git
  cd library-accounting
  ```
### Запуск через Docker Compose
```bash
docker-compose up --build
```

После запуска сервис будет доступен по адресам:
- API: http://localhost:8080/api/v1/
- Swagger UI: http://localhost:8080/swagger-ui.html (Базовый админ для авторизации: логин: admin, пароль: admin)

## Остановка сервиса
```bash
docker-compose up --build
```
Для полной очистки (с удалением томов):
```bash
docker-compose up --build -v
```