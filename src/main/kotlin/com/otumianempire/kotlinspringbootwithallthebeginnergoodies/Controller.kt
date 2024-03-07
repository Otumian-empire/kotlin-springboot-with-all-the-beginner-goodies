package com.otumianempire.kotlinspringbootwithallthebeginnergoodies

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


data class User(val email: String, val password: String)
data class Message(val message: String, val buildNumber: String)


@RequestMapping("/users")
@RestController
class Controller(
    @Value("\${com.otumianempire.kotlinspringbootwithallthebeginnergoodies.buildNumber}") val buildNumber: String,
    @Value("\${com.otumianempire.kotlinspringbootwithallthebeginnergoodies.databaseName}") val databaseName: String
) {

    val users = mutableListOf(
        User(email = "email1@gmail.com", password = "password1"),
        User(email = "email2@gmail.com", password = "password2"),
        User(email = "email3@gmail.com", password = "password3"),
        User(email = "email4@gmail.com", password = "password4"),
        User(email = "email5@gmail.com", password = "password5"),
    )

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/debug")
    fun printSomeLogs() {
        logger.trace("This is a trace log")
        logger.warn("This is a warning log")
        logger.info("This is an info log")
        logger.debug("This is a debug log")
        logger.error("This is an error log")

    }

    @GetMapping("/build")
    fun getBuildNumber() = Message(message = "The sample has a db name: $databaseName", buildNumber = buildNumber)

    @GetMapping
    fun list() = users

    @GetMapping("{id}")
    fun read(@PathVariable id: Int) = users[id]
}
