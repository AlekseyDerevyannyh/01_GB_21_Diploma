# Курс "Веб-разработка на Java"
## Дипломная работа
### Разработка back-end службы выдачи и регистрации заданий на электроснабжение электротехническому отделу в проектной организации
* **
### Автор:
Деревянных Алексей
* **

## Тестовое развёртывание приложения

Для тестового развёртывания приложения создан класс **[./src/main/java/ru/gb/repository/DataLoader.java](./src/main/java/ru/gb/repository/DataLoader.java)** со следующим содержимым:
```java
@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {
    private final UserService userService;
    private final RoleService roleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        roleService.addRole(new Role("ADMIN"));
        roleService.addRole(new Role("MANAGER"));
        roleService.addRole(new Role("USER_ISSUING"));
        roleService.addRole(new Role("USER_ACCEPTING"));

        userService.addUser(new User("admin", "admin", "Ivanov", "Ivan", "Ivanovich"));
        userService.addRoleToUser("Ivanov", "Ivan", "Ivanovich", "ADMIN");
    }
}
```
Этот класс, при запуске приложения, создаёт необходимые роли пользователей и пользователя **admin** с паролем **admin**, имеющего роль **"ADMIN"**, для доступа к REST API приложения и записывает их в базу данных. С помощью данного пользователя, посредством API приложения или через панель СУБД Adminer, можно создать других пользователей и выдать им необходимые роли.

API приложения можно просмотреть после запуска приложения по адресу http://server_ip_address:8081/swagger-ui/index.html<br>
Панель управления СУБД Adminer после запуска приложения доступна по адресу http://server_ip_address:8080/adminer<br>

Для быстрого тестового развёртывания приложения на сервере созданы файлы **[Dockerfile](Dockerfile)**:
```dockerfile
FROM maven:3.9.6-amazoncorretto-21 AS builder
COPY ./ ./
RUN mvn clean package -DskipTests
FROM openjdk:21-jdk
COPY --from=builder  /target/App-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]
```
**[docker-compose.yml](docker-compose.yml)**:
```yaml
---
version: "3.8"

services:
  db:
    image: postgres:13.2-alpine
    restart: always
    ports:
      - 5432:5432
    environment:
      POSTGRES_DB: task
      POSTGRES_USER: task
      POSTGRES_PASSWORD: secret

  adminer:
    image: adminer
    restart: always
    ports:
      - 8080:8080

  app:
    build:
      dockerfile: Dockerfile
    restart: always
    ports:
      - 8081:8081
    environment:
      SPRING_DATASOURCE_URL: "jdbc:postgresql://db:5432/task"
      SPRING_JPA_HIBERNATE_DDL-AUTO: "create-drop"
```
Для тестового развёртывания приложения необходимо из папки проекта выполнить следующую команду:
```bash
docker compose up -d --build
```
* **
## Развёртывание приложения в продакшн (общие рекомендации)
При развёртывании приложения в продакшн необходимо:
* удалить класс **[./src/main/java/ru/gb/repository/DataLoader.java](./src/main/java/ru/gb/repository/DataLoader.java)** или закомментировать его данные
* в файле **[docker-compose.yml](docker-compose.yml)** удалить или закомментировать строчку:
  ```yaml
    SPRING_JPA_HIBERNATE_DDL-AUTO: "create-drop"
  ```
  в файле настроек проекта **[./src/main/resources/application.yml](./src/main/resources/application.yml)** для этого параметра установлено значение **validate**. Это нужно, чтобы не затереть существующие данные в базе данных и не поломать её структуру
* настроить сохранение базы данных на внешнее по отношению к контейнеру хранилище
* перед первым запуском приложения привести структуру базы данных в соответствие со структурой приложения (проще всего это сделать тестовым запуском приложения с параметром **ddl-auto** принимающим значение *update* или *create*), а также внести в базу данных необходимые роли пользователей и как минимум одного пользователя с правами администратора
* создать безопасные пароли и включить шифрование паролей

В зависимости от нагрузки на приложение и от располагаемых у компании ресурсов, возможно, понадобится перенастройка развёртывания приложения на использование с базой данных, расположенной на другом сервере.

