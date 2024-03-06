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

A string name could be passed in place of *this::class.java*. *this::class.java* refers to the this class that we are
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
