package com.jtmthf.liquibasehibernatereproduction

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LiquibaseHibernateReproductionApplication

fun main(args: Array<String>) {
    runApplication<LiquibaseHibernateReproductionApplication>(*args)
}
