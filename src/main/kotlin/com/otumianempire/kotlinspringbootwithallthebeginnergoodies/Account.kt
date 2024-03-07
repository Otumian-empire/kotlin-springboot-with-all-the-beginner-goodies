package com.otumianempire.kotlinspringbootwithallthebeginnergoodies

import jakarta.persistence.*
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.web.bind.annotation.*


@Entity
@Table(name = "account")
data class Account(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0.toLong(), val name: String
)

data class ViewAccount(
    val id: Long = 0.toLong(), val name: String
)

fun Account.toView() = ViewAccount(id, name)

data class CreateAccount(val name: String)

// interface AccountRepository : JpaRepository<Account, Long> {
interface AccountRepository : CrudRepository<Account, Long> {

    // fun findAllByNameContaining(name: String): List<Account>
    fun findAllByNameContainingIgnoreCase(name: String): List<Account>

    @Query("SELECT a FROM Account a WHERE a.name LIKE concat('%', :suffix)")
    fun search(suffix: String): List<Account>
}


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