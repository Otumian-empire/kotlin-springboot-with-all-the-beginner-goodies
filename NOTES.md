# NOTES

## Applications properties

- **application.properties**: presents the default properties for the app. we can create specific environment properties
  file by doing: *application-env.properties*, where `env` is the environments name, such as *dev, staging, product,
  beta-test, etc.*, Then we have to set the `SPRING_PROFILES_ACTIVE=env`
- **application.yml**: with this, we don't have to create the separate *env*. We can use a delimiter, *---*. which will
  separate one environment from another.

**This is a sample content of the properties file**

```properties
# application.properties
spring.devtools.livereload.enabled=true
#server.port=${SERVER_PORT}
#spring.devtools.livereload.port=3000
com.otumianempire.kotlinspringbootwithallthebeginnergoodies.buildNumber=1.1-alpha
```

```properties
# application-dev.properties
spring.devtools.livereload.enabled=true
server.port=${SERVER_PORT}
#spring.devtools.livereload.port=3000
com.otumianempire.kotlinspringbootwithallthebeginnergoodies.buildNumber=1.1-dev
```

**This is the yml which has several other environments**

```yml
# application.yml
# this --- here is a delimiter which separates the two env environments
# we had to replace spring.profiles with spring.config.active.on-profile

# staging
server:
  port: 3000

com:
  otumianempire:
    kotlinspringbootwithallthebeginnergoodies:
      buildNumber: 3.0-staging-yml
      databaseName: staging-db
      password: staging-passwd

spring:
  config:
    activate:
      on-profile: staging


---
# dev
server:
  port: 4000

com:
  otumianempire:
    kotlinspringbootwithallthebeginnergoodies:
      buildNumber: 3.0-dev-yml
      databaseName: dev-db
      password: dev-passwd

spring:
  config:
    activate:
      on-profile: dev


---
# prod
server:
  port: 5000

com:
  otumianempire:
    kotlinspringbootwithallthebeginnergoodies:
      buildNumber: 3.0-prod-yml
      databaseName: prod-db
      password: prod-passwd

spring:
  config:
    activate:
      on-profile: prod
```

> In this project, *_application.properties* and *_application-dev.properties* have an underscore, '_', before it and
> that is done so that the content of it will not be replacing or interfere with the content in the *application.yml*.
> The
> system expects *application.properties* and not *_application.properties* and as such will ignore it.

## Logging

To add a logging dependency, add to
*build.gradle.kts* `implementation("org.springframework.boot:spring-boot-starter-logging")` in the dependencies. Spring
boot uses logback. Then create an instance of it where it would be used. *slf4j* logger factory is used.
This is more or less lik e

```kt
import org.slf4j.LoggerFactory

//...
val logger = LoggerFactory.getLogger(this::class.java)
```

A string name could be passed in place of *this::class.java*. *this::class.java* refers to the class that we are
in. Then use logger to do the logging. There are: trace, debug, warning (warn). info, and error.

So we can later log:

```kt
logger.trace("This is a trace log")
logger.warn("This is a warning log")
logger.info("This is an info log")
logger.debug("This is a debug log")
logger.error("This is an error log")
```

SprintBoot logs the info. we can change this in our *application.properties* or *application.yml* file

```properties
logging.level.root=TRACE
```

```yml
logging:
  level:
    root: DEBUG
```

We can set the log for some particular class

```properties
logging.level.com.otumianempire.kotlinspringbootwithallthebeginnergoodies=INFO
```

```yml
logging:
  level:
    root: ERROR
    com:
      otumianempire:
        kotlinspringbootwithallthebeginnergoodies: ERROR
```

## Database access with spring data jpa

- Add `implementation("org.springframework.boot:spring-boot-starter-data-jpa")` to dependency in `build.gradle.kts` and
  build the main module.
- Add `runtimeOnly("com.mysql:mysql-connector-j")` mysql driver
- Add `implementation("org.liquibase:liquibase-core")`, liquid base migration for migration
- Fire up mysql and check if it is properly up. Open [PhpMyAdmin](http://localhost/phpmyadmin/)
- configure the data source in the `application.properties` or `application.yml`, *spring.datasource.url=jdbc...*

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/spring_boot_api_db
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=
```

```yml
spring:
  config:
    activate:
      on-profile: prod
  datasource:
    url: jdbc:mysql://localhost:3306/spring_boot_api_db
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password:
```

We can pass the values to the environment variables since they are sensitive data.

```properties
spring.datasource.url=${DB_URL}
spring.datasource.driver-class-name=${DB_DRIVER_CLASS_NAME}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

```yml
spring:
  config:
    activate:
      on-profile: prod
  datasource:
    url: ${DB_URL}
    driver-class-name: ${DB_DRIVER_CLASS_NAME}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

Configure liquibase for migration by setting the `spring.liquibase.enable=true` and where to store the database
migration files, `spring.liquibase.change-log=db/changelog/log.xml`

Add this to`db/changelog/log.xml`:

this can be found [here](https://www.liquibase.org/get-started/quickstart)

**Table**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.4.xsd">
</databaseChangeLog>
```

If everything is set up right, then this table should be created, `DATABASECHANGELOG`.

Inside, `dev-log.xml` we can the define the table fields.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.4.xsd">

    <changeSet id="1" author="otumianempire">
        <createTable tableName="account">
            <column name="id" type="int" autoIncrement="true">
                <constraints primaryKey="true" nullable="false"/>
            </column>
            <column name="name" type="varchar(255)">
                <constraints nullable="false"/>
            </column>
        </createTable>
    </changeSet>
</databaseChangeLog>
```

This file would become larger as its contents increases, so we can then move the update into a new file then include the
new file.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.4.xsd">


    <include file="filepath"/>
</databaseChangeLog>
```

When the serer is reloaded the new table `account` will be created.

Next is to map the table to an entity.

**Entity**

```kt
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "account")
data class Account(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String
)
```

The `@Table(name="account")` is unnecessary because the class annotated with `@Entity` will find and map the
table `account` to the class `Account`. So in the case where we want to map to a different table or specify the table
name, then we can pass the `@Table(name="account")`, where we will have to pass the specific table name.

**Repository**

```kt
interface AccountRepository : CrudRepository<Account, Long>
```

**Controller**

- [jpa-query-methods](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html)

```kt
@RestController
@RequestMapping("/accounts")
class AccountController(
    val accountRepository: AccountRepository
) {

    @ExceptionHandler(Exception::class)
    fun exceptionHandling(exception: Exception): Exception {
        println(exception)
        return exception
    }

    @GetMapping
    fun list(): List<ViewAccount> = accountRepository.findAll().map { it.toView() }

    @GetMapping("{id}")
    fun read(@PathVariable id: Long) = accountRepository.findById(id)

    @GetMapping("/name/{name}")
    // fun readName(@PathVariable name: String) = accountRepository.findAllByNameContaining(name)
    fun readName(@PathVariable name: String) = accountRepository.findAllByNameContainingIgnoreCase(name)

    @GetMapping("/search/{name}")
    fun search(@PathVariable name: String) = accountRepository.search(name)

    @PostMapping
    fun create(@RequestBody createAccount: CreateAccount): ViewAccount = accountRepository.save(
        Account(name = createAccount.name)
    ).toView()
}
```

With this controller some modifications were made

```kt

// interface AccountRepository : JpaRepository<Account, Long> {
interface AccountRepository : CrudRepository<Account, Long> {

    // fun findAllByNameContaining(name: String): List<Account>
    fun findAllByNameContainingIgnoreCase(name: String): List<Account>

    @Query("SELECT a FROM Account a WHERE a.name LIKE concat('%', :suffix)")
    fun search(suffix: String): List<Account>
}
```

## Thymeleaf template engine

Thymeleaf is a template engine for rendering html.
Add `implementation("org.springframework.boot:spring-boot-starter-thymeleaf")` to te dependencies in `build.gradle.kts`
and run the build. We can then go and create a html file in `src/main/resources/templates`. We will create
an `index.html` file.

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Main Page</title>
</head>
<body>
<p>Hello world</p>
<p>This is html with thymeleaf</p>
</body>
</html
```

We will then create a controller (just like the RestController)

```kt
package com.otumianempire.kotlinspringbootwithallthebeginnergoodies

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MainController {

    @GetMapping("/")
    fun index(): String {
        return "index"
    }
}
```

Here, the `return "index"` will map to the index html file in `templates`. So when we
visit, [index page](http://localhost:3000/), we see the output for the html.

We can also pass data from the controller to the html

```kt
package com.otumianempire.kotlinspringbootwithallthebeginnergoodies

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MainController {

    @GetMapping("/")
    fun index(): String {
        return "index"
    }

    @GetMapping("/profiles")
    fun profiles(): String {
        return "profiles"
    }

    @GetMapping("/profile")
    fun profile(model: Model): String {
        model.addAttribute("id", 1)
        model.addAttribute("name", "Patrick Amin")
        model.addAttribute("firstName", "Patrick")
        // model.asMap()
        return "profile"
    }
}
```



```html
<!--profile.html-->
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Profile Page</title>
</head>
<body>
    <h1 th:text='|This is ${firstName}&#39;s profile|'></h1>
    <p th:text='|id: ${id}|'></p>
    <p th:text='|Full name: ${name}|'></p>
</body>
</html>

```