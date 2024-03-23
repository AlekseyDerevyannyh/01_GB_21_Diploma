# Курс "Веб-разработка на Java"
## Дипломная работа
### Разработка back-end службы выдачи и регистрации заданий на электроснабжение электротехническому отделу в проектной организации
* **
### Автор:
Деревянных Алексей
* **

В качестве базы данных используется PostgreSQL, запущенная на сервере в локальной сети. База данных запускается в Docker контейнере, для этого создан Dockerfile со следующим содержимым:
```bash
FROM postgres:13.2-alpine
ENV POSTGRES_DB task
ENV POSTGRES_USER task
ENV POSTGRES_PASSWORD secret
```
Для создания и запуска контейнера с БД вводим в терминале сервера следующие команды:
```bash
docker build -t task-db .
docker run --name task-db -p 5432:5432 -d task-db
```
Соответствующие параметры подключения приложения к БД были прописаны в файле **[./src/main/resources/application.yml](./src/main/resources/application.yml)**
